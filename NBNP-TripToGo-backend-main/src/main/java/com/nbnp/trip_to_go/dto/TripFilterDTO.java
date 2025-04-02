package com.nbnp.trip_to_go.dto;

import java.time.LocalDateTime;

public record TripFilterDTO(
        Boolean myTrips,
        Boolean isActive,
        LocalDateTime minStartDate
) { }
