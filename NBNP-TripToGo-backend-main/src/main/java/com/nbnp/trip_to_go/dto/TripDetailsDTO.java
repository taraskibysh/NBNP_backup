package com.nbnp.trip_to_go.dto;

import java.util.List;

public record TripDetailsDTO(
        TripDTO trip,
        List<AppUserDTO> participants,
        List<TripEventDTO> events,
        List<TaskDTO> tasks,
        List<ExpenseDTO> expenses
) {}
