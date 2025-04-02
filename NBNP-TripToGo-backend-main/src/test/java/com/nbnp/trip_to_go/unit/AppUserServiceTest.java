package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.dto.AppUserWithTripsDTO;
import com.nbnp.trip_to_go.model.Sex;
import com.nbnp.trip_to_go.dto.TripDTO;
import com.nbnp.trip_to_go.dto.mapping.AppUserMapper;
import com.nbnp.trip_to_go.dto.mapping.AppUserWithTripsMapper;
import com.nbnp.trip_to_go.dto.mapping.TripDTOMapper;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.repository.AppUserRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import com.nbnp.trip_to_go.service.AppUserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserMapper appUserMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private AppUserWithTripsMapper appUserWithTripsMapper;

    @Mock
    private TripDTOMapper tripDTOMapper;

    @InjectMocks
    private AppUserService appUserService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests for getAppUser
    @Test
    public void whenValidUserIdGiven_shouldReturnAppUserDTO() {
        // Given
        int userId = 1;
        AppUser appUser = new AppUser();
        appUser.setId(userId);
        AppUserDTO appUserDTO = new AppUserDTO(userId, "bobs", "email@example.com", "avatar.jpg", Sex.male, LocalDate.of(1999, 12, 12), "messenger", "number");

        when(appUserRepository.findById(userId)).thenReturn(Optional.of(appUser));
        when(appUserMapper.toDTO(appUser)).thenReturn(appUserDTO);

        // When
        AppUserDTO result = appUserService.getAppUser(userId);

        // Then
        assertEquals(appUserDTO, result);
    }

    @Test
    public void whenInvalidUserIdGiven_shouldThrowEntityNotFoundException() {
        // Given
        int userId = 1;

        when(appUserRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> appUserService.getAppUser(userId));
    }

    // Tests for updateAppUser
    @Test
    public void whenCurrentUserExistsAndUpdateIsSuccessful_shouldReturnUpdatedAppUserWithTripsDTO() {
        // Given
        String currentEmail = "email@example.com";
        AppUser existingAppUser = new AppUser();
        existingAppUser.setEmail(currentEmail);
        AppUserDTO appUserDTO = new AppUserDTO(1, "bobs", "email@example.com", "avatar.jpg", Sex.male, LocalDate.of(1999, 12, 12), "messenger", "number");
        List<Trip> top3Trips = Arrays.asList(new Trip(), new Trip(), new Trip());
        List<TripDTO> top3TripDTOs = Arrays.asList(
                new TripDTO(1, "Trip Title", "Description", ".jpg", "groupLink", "financeLink", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 5L),
                new TripDTO(2, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), true, 6L),
                new TripDTO(3, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(6), true, 7L)
        );
        AppUserWithTripsDTO appUserWithTripsDTO = new AppUserWithTripsDTO(1, "bobs", "email@example.com", "avatar.jpg", Sex.male, LocalDate.of(1999, 12, 12), "messenger", "number", top3TripDTOs);

        when(jwtService.getCurrentUsername()).thenReturn(currentEmail);
        when(appUserRepository.findByEmail(currentEmail)).thenReturn(Optional.of(existingAppUser));
        when(tripRepository.findTop3ByOrderByStartDateDesc()).thenReturn(top3Trips);
        when(tripDTOMapper.toTripDTO(top3Trips.get(0))).thenReturn(top3TripDTOs.get(0));
        when(tripDTOMapper.toTripDTO(top3Trips.get(1))).thenReturn(top3TripDTOs.get(1));
        when(tripDTOMapper.toTripDTO(top3Trips.get(2))).thenReturn(top3TripDTOs.get(2));
        when(appUserRepository.save(existingAppUser)).thenReturn(existingAppUser);
        when(appUserWithTripsMapper.toDTO(existingAppUser)).thenReturn(appUserWithTripsDTO);
        //when(appUserWithTripsDTO.withTop3Trips(top3TripDTOs)).thenReturn(appUserWithTripsDTO);

        // When
        AppUserWithTripsDTO result = appUserService.updateAppUser(appUserDTO);

        // Then
        assertEquals(appUserWithTripsDTO, result);
    }

    // Tests for getCurrentAppUser
    @Test
    public void whenCurrentUserExistsAndTripsAreAvailable_shouldReturnAppUserWithTripsDTO() {
        // Given
        String currentEmail = "email@example.com";
        AppUser existingAppUser = new AppUser();
        existingAppUser.setEmail(currentEmail);
        List<Trip> top3Trips = Arrays.asList(new Trip(), new Trip(), new Trip());
        List<TripDTO> top3TripDTOs = Arrays.asList(
                new TripDTO(1, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 5L),
                new TripDTO(2, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), true, 6L),
                new TripDTO(3, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(5), LocalDateTime.now().plusDays(6), true, 7L)
        );
        AppUserWithTripsDTO appUserWithTripsDTO = new AppUserWithTripsDTO(1, "bobs", "email@example.com", "avatar.jpg", Sex.male, LocalDate.of(1999, 12, 12), "messenger", "number", top3TripDTOs);

        when(jwtService.getCurrentUsername()).thenReturn(currentEmail);
        when(appUserRepository.findByEmail(currentEmail)).thenReturn(Optional.of(existingAppUser));
        when(tripRepository.findTop3ByOrderByStartDateDesc()).thenReturn(top3Trips);
        when(tripDTOMapper.toTripDTO(top3Trips.get(0))).thenReturn(top3TripDTOs.get(0));
        when(tripDTOMapper.toTripDTO(top3Trips.get(1))).thenReturn(top3TripDTOs.get(1));
        when(tripDTOMapper.toTripDTO(top3Trips.get(2))).thenReturn(top3TripDTOs.get(2));
        when(appUserWithTripsMapper.toDTO(existingAppUser)).thenReturn(appUserWithTripsDTO);
        //when(appUserWithTripsDTO.withTop3Trips(top3TripDTOs)).thenReturn(appUserWithTripsDTO);

        // When
        AppUserWithTripsDTO result = appUserService.getCurrentAppUser();

        // Then
        assertEquals(appUserWithTripsDTO, result);
    }

    @Test
    public void whenCurrentUserDoesNotExist_shouldThrowEntityNotFoundException() {
        // Given
        String currentEmail = "email@example.com";

        when(jwtService.getCurrentUsername()).thenReturn(currentEmail);
        when(appUserRepository.findByEmail(currentEmail)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> appUserService.getCurrentAppUser());
    }

    @Test
    public void whenCurrentUserExistsAndFewerThanThreeTrips_shouldReturnAvailableTrips() {
        // Given
        String currentEmail = "email@example.com";
        AppUser existingAppUser = new AppUser();
        existingAppUser.setEmail(currentEmail);
        List<Trip> top2Trips = Arrays.asList(
                new Trip(1, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, Collections.emptyList()),
                new Trip(2, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), true, Collections.emptyList())
        );
        List<TripDTO> top2TripDTOs = Arrays.asList(
                new TripDTO(1, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 5L),
                new TripDTO(2, "Trip Title", "Description", ".jpg","groupLink", "financeLink", LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), true, 6L)
        );
        AppUserWithTripsDTO appUserWithTripsDTO = new AppUserWithTripsDTO(1, "bobs", "email@example.com", "avatar.jpg", Sex.male, LocalDate.of(1999, 12, 12), "messenger", "number", top2TripDTOs);

        when(jwtService.getCurrentUsername()).thenReturn(currentEmail);
        when(appUserRepository.findByEmail(currentEmail)).thenReturn(Optional.of(existingAppUser));
        when(tripRepository.findTop3ByOrderByStartDateDesc()).thenReturn(top2Trips);
        when(appUserWithTripsMapper.toDTO(existingAppUser)).thenReturn(new AppUserWithTripsDTO(1, "bobs", "email@example.com", "avatar.jpg", Sex.male, LocalDate.of(1999, 12, 12), "messenger", "number", Collections.emptyList()));
        when(tripDTOMapper.toTripDTO(top2Trips.get(0))).thenReturn(top2TripDTOs.get(0));
        when(tripDTOMapper.toTripDTO(top2Trips.get(1))).thenReturn(top2TripDTOs.get(1));

        // When
        AppUserWithTripsDTO result = appUserService.getCurrentAppUser();

        // Then
        assertEquals(appUserWithTripsDTO, result);
    }

    @Test
    public void whenCurrentUserExistsAndNoTrips_shouldReturnEmptyTripsList() {
        // Given
        String currentEmail = "email@example.com";
        AppUser existingAppUser = new AppUser();
        existingAppUser.setEmail(currentEmail);
        List<Trip> noTrips = Collections.emptyList();
        List<TripDTO> emptyTripDTOs = Collections.emptyList();
        AppUserWithTripsDTO appUserWithTripsDTO = new AppUserWithTripsDTO(1, "bobs", "email@example.com", "avatar.jpg", Sex.male, LocalDate.of(1999, 12, 12), "messenger", "number", emptyTripDTOs);

        when(jwtService.getCurrentUsername()).thenReturn(currentEmail);
        when(appUserRepository.findByEmail(currentEmail)).thenReturn(Optional.of(existingAppUser));
        when(tripRepository.findTop3ByOrderByStartDateDesc()).thenReturn(noTrips);
        when(appUserWithTripsMapper.toDTO(existingAppUser)).thenReturn(appUserWithTripsDTO);
        //when(appUserWithTripsDTO.withTop3Trips(emptyTripDTOs)).thenReturn(appUserWithTripsDTO);

        // When
        AppUserWithTripsDTO result = appUserService.getCurrentAppUser();

        // Then
        assertEquals(appUserWithTripsDTO, result);
    }
}