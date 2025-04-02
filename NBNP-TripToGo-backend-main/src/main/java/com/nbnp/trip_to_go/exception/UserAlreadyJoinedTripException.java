package com.nbnp.trip_to_go.exception;

public class UserAlreadyJoinedTripException extends RuntimeException {
    public UserAlreadyJoinedTripException(int userId, int tripId) {
        super("User " + userId + " already joined trip " + tripId);
    }
}