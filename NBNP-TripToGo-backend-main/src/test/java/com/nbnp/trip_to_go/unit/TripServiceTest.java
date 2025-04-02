package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.dto.*;
import com.nbnp.trip_to_go.dto.mapping.ExpenseMapper;
import com.nbnp.trip_to_go.dto.mapping.TaskMapper;
import com.nbnp.trip_to_go.dto.mapping.TripEventMapper;
import com.nbnp.trip_to_go.dto.mapping.TripMapper;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Expense;
import com.nbnp.trip_to_go.model.Task;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.model.TripEvent;
import com.nbnp.trip_to_go.repository.*;
import com.nbnp.trip_to_go.repository.projection.TripParticipantsCountProjection;
import com.nbnp.trip_to_go.service.TripService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserTripRepository userTripRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private TripEventRepository tripEventRepository;

    @Mock
    private TripMapper tripMapper;

    @Mock
    private TripEventMapper tripEventMapper;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private ExpenseMapper expenseMapper;

    @Mock
    private JwtService jwtService;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private TripService tripService;

    private AutoCloseable closeable;
    private Trip trip;
    private TripDTO tripDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        trip = new Trip();
        trip.setId(1);
        trip.setTitle("Spain adventure");
        trip.setTripDescription("Desc");
        trip.setGroupLink("group");
        trip.setFinanceLink("finance");
        trip.setStartDate(LocalDateTime.now().plusDays(1));
        trip.setEndDate(LocalDateTime.now().plusDays(7));
        trip.setIsActive(true);

        tripDTO = new TripDTO(
                1, "Spain adventure", "Desc", "photo.jpg", "group", "finance",
                trip.getStartDate(), trip.getEndDate(), true, null
        );

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);
        when(tripMapper.toDTO(any(Trip.class))).thenReturn(tripDTO);
        when(tripMapper.toEntity(any(TripDTO.class))).thenReturn(trip);
        when(tripEventMapper.toDTO(any())).thenReturn(mock(TripEventDTO.class));
        when(taskMapper.toDTO(any())).thenReturn(mock(TaskDTO.class));
        when(expenseMapper.toDTO(any())).thenReturn(mock(ExpenseDTO.class));
        when(userTripRepository.findByTripId(anyInt())).thenReturn(Collections.emptyList());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testCreateTrip_ShouldReturnCreatedTrip() {
        AppUser user = new AppUser();
        user.setEmail("johndoe@gmail.com");

        when(jwtService.getCurrentUsername()).thenReturn(user.getEmail());
        when(appUserRepository.findByEmail("johndoe@gmail.com")).thenReturn(Optional.of(user));

        TripDTO created = tripService.createTrip(tripDTO);
        assertNotNull(created);
        assertEquals("Spain adventure", created.title());
    }

    @Test
    void testGetTripDetails_ShouldReturnTripDetails() {
        TripEvent tripEvent = new TripEvent();
        tripEvent.setTitle("Spain adventure");
        tripEvent.setEventDate(LocalDateTime.now().plusDays(1));
        tripEvent.setEventDescription("Desc");
        when(tripEventRepository.findByTripId(anyInt())).thenReturn(List.of(tripEvent));

        Task task = new Task(1, "buy", "desc", LocalDateTime.of(2026, 8, 10, 10, 30), false, trip, null);
        when(taskRepository.findByTripId(anyInt())).thenReturn(List.of(task));

        Expense expense = new Expense();
        expense.setId(1);
        expense.setDescription("Dinner");
        expense.setAmount(BigDecimal.valueOf(100.50));
        expense.setPaymentDate(LocalDateTime.of(2024, 3, 8, 12, 0));
        when(expenseRepository.findByTripId(anyInt())).thenReturn(List.of(expense));

        TripDetailsDTO details = tripService.getTripDetails(1);
        assertNotNull(details);
        assertEquals("Spain adventure", details.trip().title());
    }

    @Test
    void testGetTripDetails_ShouldThrow_WhenTripNotFound() {
        when(tripRepository.findById(2)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> tripService.getTripDetails(2)
        );
        assertEquals("Trip not found with id 2", ex.getMessage());
    }

    @Test
    void testDeleteTrip_ShouldSetActiveFalse() {
        tripService.deleteTrip(1);
        verify(tripRepository, times(1)).save(trip);
        assertFalse(trip.getIsActive());
    }

    @Test
    void testDeleteTrip_ShouldThrow_WhenTripNotFound() {
        when(tripRepository.findById(1)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> tripService.deleteTrip(1)
        );
        assertEquals("Trip not found with id 1", ex.getMessage());
    }

    @Test
    void getAllTrips_whenFilterIsValid_shouldReturnTrips() {
        TripFilterDTO filter = mock(TripFilterDTO.class);

        List<Trip> expectedTrips = List.of(
                new Trip(1, "beach_vacation", null, null, null, null,
                        LocalDateTime.now().minusDays(10),
                        LocalDateTime.now().plusDays(10), true, Collections.emptyList()),
                new Trip(2, "mountain_vacation", null, null, null, null,
                        LocalDateTime.now().minusDays(5),
                        LocalDateTime.now().plusDays(5), true, Collections.emptyList())
        );


        when(jwtService.getCurrentUsername()).thenReturn("test@example.com");

        AppUser currentUser = new AppUser();
        currentUser.setId(1);
        currentUser.setEmail("test@example.com");

        when(appUserRepository.findByEmail("test@example.com")).thenReturn(Optional.of(currentUser));
        when(tripRepository.findAll(ArgumentMatchers.<Specification<Trip>>any())).thenReturn(expectedTrips);

        List<Trip> actualTrips = tripService.getAllTrips(filter);

        assertEquals(expectedTrips.size(), actualTrips.size());
        verify(tripRepository).findAll(ArgumentMatchers.<Specification<Trip>>any());
    }

    @Test
    void countParticipantsById_whenTripIdsAreValid_shouldReturnCorrectCounts() {
        List<Integer> tripIds = List.of(1, 2);
        TripParticipantsCountProjection projection1 = mock(TripParticipantsCountProjection.class);
        TripParticipantsCountProjection projection2 = mock(TripParticipantsCountProjection.class);

        when(projection1.getTripId()).thenReturn(1);
        when(projection1.getParticipantCount()).thenReturn(5L);
        when(projection2.getTripId()).thenReturn(2);
        when(projection2.getParticipantCount()).thenReturn(3L);

        when(tripRepository.countParticipantsByTripIds(tripIds)).thenReturn(List.of(projection1, projection2));

        Map<Integer, Long> result = tripService.countParticipantsById(tripIds);

        assertEquals(2, result.size());
        assertEquals(5L, result.get(1));
        assertEquals(3L, result.get(2));
    }

    @Test
    void countParticipantsById_whenTripIdsAreNull_shouldThrowException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> tripService.countParticipantsById(null));
        assertEquals("Trip IDs cannot be null", exception.getMessage());
    }
}
