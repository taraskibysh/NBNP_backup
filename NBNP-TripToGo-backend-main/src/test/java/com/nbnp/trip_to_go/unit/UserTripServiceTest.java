package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.exception.UserAlreadyJoinedTripException;
import com.nbnp.trip_to_go.exception.UserNotFoundException;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.model.UserTrip;
import com.nbnp.trip_to_go.repository.AppUserRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import com.nbnp.trip_to_go.repository.UserTripRepository;
import com.nbnp.trip_to_go.service.UserTripService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTripServiceTest {

    @Mock
    private UserTripRepository userTripRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserTripService userTripService;

    private AutoCloseable closeable;
    private Trip trip;
    private AppUser user;
    private UserTrip userTrip;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        trip = createTestTrip();
        user = createTestUser();
        userTrip = createTestUserTrip();
    }

    private Trip createTestTrip() {
        Trip trip = new Trip();
        trip.setTitle("Trip to Italy");
        return trip;
    }

    private AppUser createTestUser() {
        AppUser user = new AppUser();
        user.setId(1);
        user.setFullName("John Doe");
        user.setEmail("johndoe@gmail.com");
        return user;
    }

    private UserTrip createTestUserTrip() {
        UserTrip userTrip = new UserTrip();
        userTrip.setTrip(trip);
        userTrip.setUser(user);
        userTrip.setOwner(false);
        return userTrip;
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void givenValidUserAndTrip_whenJoinTrip_thenUserAdded() {
        when(jwtService.getCurrentUsername()).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(appUserRepository.findById(1)).thenReturn(Optional.of(user));
        when(userTripRepository.findByTripIdAndUserId(1, 1)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userTripService.joinTrip(1));
        verify(userTripRepository, times(1)).save(any(UserTrip.class));
    }

    @Test
    void givenNonExistingTrip_whenJoinTrip_thenThrowException() {
        when(jwtService.getCurrentUsername()).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));

        when(tripRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userTripService.joinTrip(1));
        assertEquals("Trip with ID 1 does not exist", ex.getMessage());
    }

    @Test
    void givenNonExistingUser_whenJoinTrip_thenThrowException() {
        when(jwtService.getCurrentUsername()).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.empty());

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));

        RuntimeException ex = assertThrows(UserNotFoundException.class, () -> userTripService.joinTrip(1));
        assertEquals("No user found with email: johndoe@gmail.com", ex.getMessage());
    }

    @Test
    void givenUserAlreadyJoined_whenJoinTrip_thenThrowException() {
        when(jwtService.getCurrentUsername()).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(userTripRepository.existsByTripIdAndUserId(1, 1)).thenReturn(Boolean.TRUE);

        UserAlreadyJoinedTripException ex = assertThrows(UserAlreadyJoinedTripException.class, () -> userTripService.joinTrip(1));
        assertEquals("User 1 already joined trip 1", ex.getMessage());
    }

    @Test
    void givenUserInTrip_whenLeaveTrip_thenUserIsRemoved() {
        when(jwtService.getCurrentUsername()).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));

        when(userTripRepository.findByTripIdAndUserId(1, 1)).thenReturn(Optional.of(userTrip));
        assertDoesNotThrow(() -> userTripService.leaveTrip(1));
        verify(userTripRepository, times(1)).delete(userTrip);
    }

    @Test
    void givenUserNotInTrip_whenLeaveTrip_thenThrowException() {
        when(jwtService.getCurrentUsername()).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));
        when(userTripRepository.existsByTripIdAndUserId(1, 1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> userTripService.leaveTrip(1));
        assertEquals("User johndoe@gmail.com is not part of this trip", ex.getMessage());

        verify(userTripRepository, times(0)).deleteByTripIdAndUserId(1, 1);
    }
}
