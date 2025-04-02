package com.nbnp.trip_to_go.exception.response;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldErrorDetail> errors
) {
    public static ValidationErrorResponse of(HttpStatus status, String message, String path, List<FieldErrorDetail> errors) {
        return new ValidationErrorResponse(Instant.now(), status.value(), status.getReasonPhrase(), message, path, errors);
    }
}
