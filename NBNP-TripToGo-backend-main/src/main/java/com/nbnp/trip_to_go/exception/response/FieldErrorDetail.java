package com.nbnp.trip_to_go.exception.response;

public record FieldErrorDetail(
        String field,
        Object rejectedValue,
        String message
) { }