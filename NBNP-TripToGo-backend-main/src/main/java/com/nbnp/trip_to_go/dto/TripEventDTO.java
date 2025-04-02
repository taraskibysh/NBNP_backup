package com.nbnp.trip_to_go.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TripEventDTO(
        Integer id,
        @NotBlank String title,
        String eventDescription,
        @NotNull LocalDateTime eventDate
) {}
