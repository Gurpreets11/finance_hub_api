package com.finance.api.repository;

import com.finance.api.entity.Income;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    Page<Income> findByUserIdOrderByTransactionDateDesc(Long userId, Pageable pageable);

    List<Income> findTop5ByUserIdOrderByTransactionDateDesc(Long userId);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId " +
           "AND i.transactionDate BETWEEN :start AND :end")
    BigDecimal sumAmountByUserIdAndDateRange(Long userId, LocalDate start, LocalDate end);

    @Query("SELECT SUM(i.amount) FROM Income i WHERE i.user.id = :userId")
    BigDecimal sumTotalByUserId(Long userId);
}
