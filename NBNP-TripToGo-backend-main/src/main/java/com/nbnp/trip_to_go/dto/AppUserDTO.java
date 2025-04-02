package com.nbnp.trip_to_go.dto;

import com.nbnp.trip_to_go.model.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AppUserDTO(

        Integer id,

        @NotBlank(message = "Full name is required")
        String fullName,

        @Email
        String email,

        String avatarImage,

        @NotNull(message = "Sex is required")
        Sex sex,

        @NotNull(message = "Date of birth is required")
        LocalDate dateOfBirth,

        String messengerLink,

        String phoneNumber
){}
