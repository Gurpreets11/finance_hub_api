package com.finance.api.dto.request;

import com.finance.api.entity.Investment;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvestmentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Investment type is required")
    private Investment.InvestmentType type;

    @NotNull(message = "Invested amount is required")
    @DecimalMin(value = "0.01")
    private BigDecimal investedAmount;

    @NotNull(message = "Current value is required")
    @DecimalMin(value = "0.00")
    private BigDecimal currentValue;

    private LocalDate startDate;

    private LocalDate maturityDate;

    private String notes;
}
