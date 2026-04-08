package com.finance.api.dto.request;

import com.finance.api.entity.Loan;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class LoanRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Loan type is required")
    private Loan.LoanType type;

    @NotNull @DecimalMin("0.01")
    private BigDecimal totalAmount;

    @NotNull @DecimalMin("0.00")
    private BigDecimal outstandingAmount;

    @NotNull @DecimalMin("0.01")
    private BigDecimal emiAmount;

    private BigDecimal interestRate;

    @NotNull(message = "EMI due date is required")
    private LocalDate emiDueDate;

    private LocalDate startDate;

    private LocalDate endDate;

    private String lenderName;
}
