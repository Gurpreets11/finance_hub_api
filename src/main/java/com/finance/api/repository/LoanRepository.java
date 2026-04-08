package com.finance.api.repository;

import com.finance.api.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    Page<Loan> findByUserIdOrderByEmiDueDateAsc(Long userId, Pageable pageable);

    // Loans with upcoming EMIs in the next N days
    List<Loan> findByUserIdAndEmiDueDateBetweenOrderByEmiDueDateAsc(
            Long userId, LocalDate from, LocalDate to);

    @Query("SELECT SUM(l.outstandingAmount) FROM Loan l WHERE l.user.id = :userId")
    BigDecimal sumOutstandingByUserId(Long userId);

    @Query("SELECT SUM(l.emiAmount) FROM Loan l WHERE l.user.id = :userId")
    BigDecimal sumMonthlyEmiByUserId(Long userId);
}
