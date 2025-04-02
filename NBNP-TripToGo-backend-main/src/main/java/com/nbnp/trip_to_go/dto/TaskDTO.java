package com.nbnp.trip_to_go.dto;

import com.nbnp.trip_to_go.model.AppUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record TaskDTO(
        Integer id,
        @NotBlank(message = "Task name is required")
        String taskName,

        String taskDescription,

        @NotNull(message = "Deadline is required")
        LocalDateTime taskDeadline,

        @NotNull(message = "Task completion status is required")
        Boolean isDone,

        AppUserDTO user


) {}
