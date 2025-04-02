package com.nbnp.trip_to_go.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseDTO(
        @NotBlank String description,
        @NotNull BigDecimal amount,
        LocalDateTime paymentDate
) {}
