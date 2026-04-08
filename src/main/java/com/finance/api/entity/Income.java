package com.finance.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "incomes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;           // e.g. "Monthly Salary - TCS"

    @Column(nullable = false)
    private String category;        // SALARY, RENTAL, DIVIDEND, FREELANCE, OTHER

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate transactionDate;

    private String description;

    private Boolean isRecurring;    // monthly salary = true

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private IncomeCategory categoryEnum = IncomeCategory.OTHER;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum IncomeCategory {
        SALARY, RENTAL, DIVIDEND, FREELANCE, BUSINESS, OTHER
    }
}
