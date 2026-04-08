package com.finance.api.config;

import com.finance.api.entity.*;
import com.finance.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Seeds demo data so you can test the APIs immediately after startup.
 * Only runs under the default / dev profile. Remove for production.
 *
 * Demo credentials:
 *   email: demo@finance.com
 *   password: password123
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    @Bean
    @Profile("!prod")   // skip in prod
    CommandLineRunner seedData(UserRepository userRepo,
                               IncomeRepository incomeRepo,
                               ExpenseRepository expenseRepo,
                               InvestmentRepository investmentRepo,
                               LoanRepository loanRepo,
                               PasswordEncoder encoder) {
        return args -> {
            if (userRepo.existsByEmail("demo@finance.com")) return;

            // ── User ───────────────────────────────────────────────────────────
            User user = userRepo.save(User.builder()
                    .fullName("Raj Kumar")
                    .email("demo@finance.com")
                    .password(encoder.encode("password123"))
                    .role(User.Role.USER)
                    .build());

            LocalDate today = LocalDate.now();

            // ── Incomes ────────────────────────────────────────────────────────
            incomeRepo.save(Income.builder().user(user)
                    .title("Monthly Salary - TCS")
                    .category("Salary")
                    .categoryEnum(Income.IncomeCategory.SALARY)
                    .amount(new BigDecimal("485000"))
                    .transactionDate(today.withDayOfMonth(1))
                    .isRecurring(true).build());

            incomeRepo.save(Income.builder().user(user)
                    .title("Rental Income - Flat B12")
                    .category("Rental")
                    .categoryEnum(Income.IncomeCategory.RENTAL)
                    .amount(new BigDecimal("17000"))
                    .transactionDate(today.minusDays(5))
                    .isRecurring(true).build());

            incomeRepo.save(Income.builder().user(user)
                    .title("HDFC Bank Dividend")
                    .category("Dividend")
                    .categoryEnum(Income.IncomeCategory.DIVIDEND)
                    .amount(new BigDecimal("3500"))
                    .transactionDate(today.minusDays(10))
                    .isRecurring(false).build());

            // ── Expenses ───────────────────────────────────────────────────────
            expenseRepo.save(Expense.builder().user(user)
                    .title("House Rent")
                    .category("Rent")
                    .categoryEnum(Expense.ExpenseCategory.RENT)
                    .amount(new BigDecimal("18000"))
                    .transactionDate(today.withDayOfMonth(5)).build());

            expenseRepo.save(Expense.builder().user(user)
                    .title("Groceries & Dining")
                    .category("Food & Dining")
                    .categoryEnum(Expense.ExpenseCategory.FOOD_DINING)
                    .amount(new BigDecimal("8500"))
                    .transactionDate(today.minusDays(3)).build());

            expenseRepo.save(Expense.builder().user(user)
                    .title("Fuel & Uber")
                    .category("Transport")
                    .categoryEnum(Expense.ExpenseCategory.TRANSPORT)
                    .amount(new BigDecimal("3200"))
                    .transactionDate(today.minusDays(7)).build());

            // ── Investments ────────────────────────────────────────────────────
            investmentRepo.save(Investment.builder().user(user)
                    .name("Mirae Asset Large Cap Fund")
                    .type(Investment.InvestmentType.MUTUAL_FUND)
                    .investedAmount(new BigDecimal("150000"))
                    .currentValue(new BigDecimal("180000"))
                    .returnPercentage(new BigDecimal("20.00"))
                    .startDate(today.minusYears(2)).build());

            investmentRepo.save(Investment.builder().user(user)
                    .name("Reliance Industries")
                    .type(Investment.InvestmentType.STOCK)
                    .investedAmount(new BigDecimal("82000"))
                    .currentValue(new BigDecimal("92000"))
                    .returnPercentage(new BigDecimal("12.20"))
                    .startDate(today.minusYears(1)).build());

            investmentRepo.save(Investment.builder().user(user)
                    .name("SBI Fixed Deposit")
                    .type(Investment.InvestmentType.FIXED_DEPOSIT)
                    .investedAmount(new BigDecimal("200000"))
                    .currentValue(new BigDecimal("218000"))
                    .returnPercentage(new BigDecimal("9.00"))
                    .startDate(today.minusMonths(6))
                    .maturityDate(today.plusMonths(6)).build());

            // ── Loans ──────────────────────────────────────────────────────────
            loanRepo.save(Loan.builder().user(user)
                    .name("SBI Home Loan")
                    .type(Loan.LoanType.HOME)
                    .totalAmount(new BigDecimal("5000000"))
                    .outstandingAmount(new BigDecimal("3800000"))
                    .emiAmount(new BigDecimal("45000"))
                    .interestRate(new BigDecimal("8.50"))
                    .emiDueDate(today.plusDays(1))
                    .lenderName("State Bank of India")
                    .startDate(today.minusYears(3)).build());

            loanRepo.save(Loan.builder().user(user)
                    .name("HDFC Personal Loan")
                    .type(Loan.LoanType.PERSONAL)
                    .totalAmount(new BigDecimal("300000"))
                    .outstandingAmount(new BigDecimal("230000"))
                    .emiAmount(new BigDecimal("9500"))
                    .interestRate(new BigDecimal("14.00"))
                    .emiDueDate(today.plusDays(10))
                    .lenderName("HDFC Bank")
                    .startDate(today.minusMonths(8)).build());

            loanRepo.save(Loan.builder().user(user)
                    .name("Axis Blue Chip SIP")
                    .type(Loan.LoanType.OTHER)
                    .totalAmount(new BigDecimal("60000"))
                    .outstandingAmount(new BigDecimal("60000"))
                    .emiAmount(new BigDecimal("5000"))
                    .emiDueDate(today.plusDays(10))
                    .lenderName("Axis Mutual Fund")
                    .startDate(today.minusMonths(1)).build());

            log.info("✅ Demo data seeded. Login: demo@finance.com / password123");
        };
    }
}
