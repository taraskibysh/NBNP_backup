package com.nbnp.trip_to_go.repository.projection;

public interface TripParticipantsCountProjection {
    Integer getTripId();
    Long getParticipantCount();
}
