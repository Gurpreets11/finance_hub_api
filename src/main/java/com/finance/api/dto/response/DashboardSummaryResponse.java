package com.finance.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardSummaryResponse {

    // ── Net Worth ──────────────────────────────────────
    private BigDecimal netWorth;              // investments - loans
    private BigDecimal totalInvestments;
    private BigDecimal totalLoans;

    // ── This Month Stats ──────────────────────────────
    private MonthStat income;
    private MonthStat expenses;
    private MonthStat investments;
    private MonthStat loans;

    // ── Recent Transactions (last 3 each) ─────────────
    private List<RecentTransactionItem> recentIncomes;
    private List<RecentTransactionItem> recentExpenses;

    // ── Top Investments (top 3 by value) ──────────────
    private List<TopInvestmentItem> topInvestments;

    // ── Upcoming Payments (next 30 days) ──────────────
    private List<UpcomingPaymentItem> upcomingPayments;

    // ── Inner DTOs ────────────────────────────────────

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class MonthStat {
        private BigDecimal amount;
        private BigDecimal changePercent;     // vs last month
        private String trend;                 // UP / DOWN / FLAT
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class RecentTransactionItem {
        private Long id;
        private String title;
        private String category;
        private BigDecimal amount;
        private LocalDate date;
        private String type;                  // INCOME / EXPENSE
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TopInvestmentItem {
        private Long id;
        private String name;
        private String type;
        private BigDecimal currentValue;
        private BigDecimal returnPercent;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class UpcomingPaymentItem {
        private Long id;
        private String name;
        private String type;                  // LOAN / INVESTMENT_SIP
        private BigDecimal amount;
        private LocalDate dueDate;
        private int daysUntilDue;
    }
}
