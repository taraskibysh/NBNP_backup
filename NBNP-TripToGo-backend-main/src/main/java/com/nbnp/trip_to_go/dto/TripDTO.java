package com.nbnp.trip_to_go.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TripDTO(
        Integer id,
        @NotBlank @Size(max = 250) String title,
        String tripDescription,
        String avatarImage,
        String groupLink,
        String financeLink,
        @NotNull @Future(message = "Start date must be in the future") LocalDateTime startDate,
        @NotNull @Future(message = "End date must be in the future") LocalDateTime endDate,
        @NotNull Boolean isActive,
        Long participantsCount
) {
    public TripDTO withTripParticipants(Long participants) {
        return new TripDTO(this.id, this.title, this.tripDescription, this.avatarImage, this.groupLink, this.financeLink , this.startDate, this.endDate, this.isActive, participants);
    }

}
