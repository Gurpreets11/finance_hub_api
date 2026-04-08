package com.finance.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;           // e.g. "House Rent"

    @Column(nullable = false)
    private String category;        // RENT, FOOD, TRANSPORT, etc.

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate transactionDate;

    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ExpenseCategory categoryEnum = ExpenseCategory.OTHER;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum ExpenseCategory {
        RENT, FOOD_DINING, TRANSPORT, UTILITIES, ENTERTAINMENT,
        HEALTH, EDUCATION, SHOPPING, OTHER
    }
}
