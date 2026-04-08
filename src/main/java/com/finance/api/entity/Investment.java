package com.finance.api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "investments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;            // e.g. "Mirae Asset Large Cap Fund"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvestmentType type;    // MUTUAL_FUND, STOCK, FIXED_DEPOSIT, etc.

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal investedAmount;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal currentValue;

    // Calculated: (currentValue - investedAmount) / investedAmount * 100
    @Column(precision = 8, scale = 2)
    private BigDecimal returnPercentage;

    private LocalDate startDate;

    private LocalDate maturityDate;  // for FDs

    private String notes;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum InvestmentType {
        MUTUAL_FUND, STOCK, FIXED_DEPOSIT, GOLD, REAL_ESTATE, CRYPTO, OTHER
    }
}
