package com.nbnp.trip_to_go.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String email) {
        super("No user found with email: " + email);
    }
}