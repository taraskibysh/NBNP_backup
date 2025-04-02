package com.nbnp.trip_to_go.dto;

import com.nbnp.trip_to_go.model.Sex;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record AppUserWithTripsDTO(
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
        String phoneNumber,
        List<TripDTO> top3Trips
) {
    public AppUserWithTripsDTO withTop3Trips(List<TripDTO> trips){
        return new AppUserWithTripsDTO(this.id, this.fullName, this.email, this.avatarImage, this.sex, this.dateOfBirth, this.messengerLink, this.phoneNumber, trips);
    }
}
