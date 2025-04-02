package com.nbnp.trip_to_go.dto;

import com.nbnp.trip_to_go.model.SortType;
import jakarta.validation.constraints.NotNull;

public record TaskFilterDTO(
        @NotNull(message = "Trip ID cannot be null and must be a valid integer.")
        Integer tripId,
        Boolean isDone,
        SortType sortType,
        Boolean myTasks
) {}
