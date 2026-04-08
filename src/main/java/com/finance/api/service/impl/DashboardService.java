package com.finance.api.service.impl;

import com.finance.api.dto.response.DashboardSummaryResponse;
import com.finance.api.dto.response.DashboardSummaryResponse.*;
import com.finance.api.entity.*;
import com.finance.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final InvestmentRepository investmentRepository;
    private final LoanRepository loanRepository;

    public DashboardSummaryResponse getSummary(Long userId) {

        LocalDate today         = LocalDate.now();
        LocalDate monthStart    = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate monthEnd      = today.with(TemporalAdjusters.lastDayOfMonth());
        LocalDate lastMonthStart = monthStart.minusMonths(1);
        LocalDate lastMonthEnd   = monthStart.minusDays(1);

        // ── Net Worth ──────────────────────────────────────────────────────────
        BigDecimal totalInvestments = nullSafe(investmentRepository.sumCurrentValueByUserId(userId));
        BigDecimal totalLoans       = nullSafe(loanRepository.sumOutstandingByUserId(userId));
        BigDecimal netWorth         = totalInvestments.subtract(totalLoans);

        // ── This Month ────────────────────────────────────────────────────────
        BigDecimal thisMonthIncome   = nullSafe(incomeRepository.sumAmountByUserIdAndDateRange(userId, monthStart, monthEnd));
        BigDecimal lastMonthIncome   = nullSafe(incomeRepository.sumAmountByUserIdAndDateRange(userId, lastMonthStart, lastMonthEnd));

        BigDecimal thisMonthExpense  = nullSafe(expenseRepository.sumAmountByUserIdAndDateRange(userId, monthStart, monthEnd));
        BigDecimal lastMonthExpense  = nullSafe(expenseRepository.sumAmountByUserIdAndDateRange(userId, lastMonthStart, lastMonthEnd));

        BigDecimal totalInvestedAmt  = nullSafe(investmentRepository.sumInvestedAmountByUserId(userId));
        BigDecimal monthlyEmi        = nullSafe(loanRepository.sumMonthlyEmiByUserId(userId));

        // ── Recent Transactions ───────────────────────────────────────────────
        List<RecentTransactionItem> recentIncomes = incomeRepository
                .findTop5ByUserIdOrderByTransactionDateDesc(userId)
                .stream()
                .map(this::toRecentIncome)
                .collect(Collectors.toList());

        List<RecentTransactionItem> recentExpenses = expenseRepository
                .findTop5ByUserIdOrderByTransactionDateDesc(userId)
                .stream()
                .map(this::toRecentExpense)
                .collect(Collectors.toList());

        // ── Top Investments ───────────────────────────────────────────────────
        List<TopInvestmentItem> topInvestments = investmentRepository
                .findTop3ByUserIdOrderByCurrentValueDesc(userId)
                .stream()
                .map(this::toTopInvestment)
                .collect(Collectors.toList());

        // ── Upcoming Payments (EMIs due in next 30 days) ──────────────────────
        List<UpcomingPaymentItem> upcomingPayments = loanRepository
                .findByUserIdAndEmiDueDateBetweenOrderByEmiDueDateAsc(userId, today, today.plusDays(30))
                .stream()
                .map(l -> toUpcomingPayment(l, today))
                .collect(Collectors.toList());

        return DashboardSummaryResponse.builder()
                .netWorth(netWorth)
                .totalInvestments(totalInvestments)
                .totalLoans(totalLoans)
                .income(buildMonthStat(thisMonthIncome, lastMonthIncome))
                .expenses(buildMonthStat(thisMonthExpense, lastMonthExpense))
                .investments(MonthStat.builder()
                        .amount(totalInvestedAmt)
                        .trend("UP")
                        .build())
                .loans(MonthStat.builder()
                        .amount(monthlyEmi)
                        .trend("FLAT")
                        .build())
                .recentIncomes(recentIncomes)
                .recentExpenses(recentExpenses)
                .topInvestments(topInvestments)
                .upcomingPayments(upcomingPayments)
                .build();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private MonthStat buildMonthStat(BigDecimal current, BigDecimal previous) {
        BigDecimal changePercent = BigDecimal.ZERO;
        String trend = "FLAT";

        if (previous.compareTo(BigDecimal.ZERO) != 0) {
            changePercent = current.subtract(previous)
                    .divide(previous, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
            trend = changePercent.compareTo(BigDecimal.ZERO) >= 0 ? "UP" : "DOWN";
        }

        return MonthStat.builder()
                .amount(current)
                .changePercent(changePercent)
                .trend(trend)
                .build();
    }

    private RecentTransactionItem toRecentIncome(Income i) {
        return RecentTransactionItem.builder()
                .id(i.getId())
                .title(i.getTitle())
                .category(i.getCategory())
                .amount(i.getAmount())
                .date(i.getTransactionDate())
                .type("INCOME")
                .build();
    }

    private RecentTransactionItem toRecentExpense(Expense e) {
        return RecentTransactionItem.builder()
                .id(e.getId())
                .title(e.getTitle())
                .category(e.getCategory())
                .amount(e.getAmount().negate())   // expenses shown as negative
                .date(e.getTransactionDate())
                .type("EXPENSE")
                .build();
    }

    private TopInvestmentItem toTopInvestment(Investment inv) {
        BigDecimal returnPct = BigDecimal.ZERO;
        if (inv.getInvestedAmount().compareTo(BigDecimal.ZERO) != 0) {
            returnPct = inv.getCurrentValue()
                    .subtract(inv.getInvestedAmount())
                    .divide(inv.getInvestedAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .setScale(2, RoundingMode.HALF_UP);
        }
        return TopInvestmentItem.builder()
                .id(inv.getId())
                .name(inv.getName())
                .type(inv.getType().name())
                .currentValue(inv.getCurrentValue())
                .returnPercent(returnPct)
                .build();
    }

    private UpcomingPaymentItem toUpcomingPayment(Loan loan, LocalDate today) {
        return UpcomingPaymentItem.builder()
                .id(loan.getId())
                .name(loan.getName())
                .type(loan.getType().name())
                .amount(loan.getEmiAmount())
                .dueDate(loan.getEmiDueDate())
                .daysUntilDue((int) today.until(loan.getEmiDueDate(),
                        java.time.temporal.ChronoUnit.DAYS))
                .build();
    }

    private BigDecimal nullSafe(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
}
