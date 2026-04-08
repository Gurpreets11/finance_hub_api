package com.finance.api.repository;

import com.finance.api.entity.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Page<Expense> findByUserIdOrderByTransactionDateDesc(Long userId, Pageable pageable);

    List<Expense> findTop5ByUserIdOrderByTransactionDateDesc(Long userId);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId " +
           "AND e.transactionDate BETWEEN :start AND :end")
    BigDecimal sumAmountByUserIdAndDateRange(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
    BigDecimal sumTotalByUserId(Long userId);
}
