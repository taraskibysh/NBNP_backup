package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.dto.AppUserWithTripsDTO;
import com.nbnp.trip_to_go.dto.TripDTO;
import com.nbnp.trip_to_go.dto.mapping.AppUserMapper;
import com.nbnp.trip_to_go.dto.mapping.AppUserWithTripsMapper;
import com.nbnp.trip_to_go.dto.mapping.TripDTOMapper;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.repository.AppUserRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;
    private final JwtService jwtService;
    private final TripRepository tripRepository;
    private final AppUserWithTripsMapper appUserWithTripsMapper;
    private final TripDTOMapper tripDTOMapper;

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper, JwtService jwtService, TripRepository tripRepository, AppUserWithTripsMapper appUserWithTripsMapper, TripDTOMapper tripDTOMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.jwtService = jwtService;
        this.tripRepository = tripRepository;
        this.appUserWithTripsMapper = appUserWithTripsMapper;
        this.tripDTOMapper = tripDTOMapper;
    }

    public AppUserDTO getAppUser(int userId) {
        AppUser appUser = appUserRepository.findById(userId).
                orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
        return appUserMapper.toDTO(appUser);
    }

    public AppUserWithTripsDTO updateAppUser(AppUserDTO appUserDTO) {
        String currentEmail = jwtService.getCurrentUsername();

        AppUser existingAppUser = appUserRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + currentEmail + " not found"));

        existingAppUser.setFullName(appUserDTO.fullName());
        existingAppUser.setAvatarImage(appUserDTO.avatarImage());
        existingAppUser.setMessengerLink(appUserDTO.messengerLink());
        existingAppUser.setPhoneNumber(appUserDTO.phoneNumber());
        existingAppUser.setSex(appUserDTO.sex());
        existingAppUser.setDateOfBirth(appUserDTO.dateOfBirth());

        List<TripDTO> top3Trips = tripRepository.findTop3ByOrderByStartDateDesc().stream().map(tripDTOMapper::toTripDTO).collect(Collectors.toList());
        return appUserWithTripsMapper.toDTO(appUserRepository.save(existingAppUser)).withTop3Trips(top3Trips);
    }

    public AppUserWithTripsDTO getCurrentAppUser() {
        String currentEmail = jwtService.getCurrentUsername();

        AppUser existingCurrentAppUser = appUserRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + currentEmail + " not found"));

        List<Trip> top3Trips = tripRepository.findTop3ByOrderByStartDateDesc();
        AppUserWithTripsDTO existingCurrentAppUserDTO = appUserWithTripsMapper.toDTO(existingCurrentAppUser);
        return existingCurrentAppUserDTO.withTop3Trips(top3Trips.stream().map(tripDTOMapper::toTripDTO).collect(Collectors.toList()));
    }
}
