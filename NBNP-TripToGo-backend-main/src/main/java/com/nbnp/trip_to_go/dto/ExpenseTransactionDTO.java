package com.nbnp.trip_to_go.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExpenseTransactionDTO(
        @NotNull BigDecimal userAmount,
        @NotNull Boolean done
) {}

