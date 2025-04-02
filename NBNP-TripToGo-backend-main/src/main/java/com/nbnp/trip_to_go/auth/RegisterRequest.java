package com.nbnp.trip_to_go.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String fullName,
        @Email (message = "Invalid email format") String email,
        @NotBlank String password,
        @NotBlank String passwordConfirmation
) { }
