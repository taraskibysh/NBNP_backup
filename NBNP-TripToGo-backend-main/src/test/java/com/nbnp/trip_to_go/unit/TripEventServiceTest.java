package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.dto.TripEventDTO;
import com.nbnp.trip_to_go.dto.mapping.TripEventMapper;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.model.TripEvent;
import com.nbnp.trip_to_go.repository.TripEventRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import com.nbnp.trip_to_go.service.TripEventService;
import com.nbnp.trip_to_go.service.UserTripService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripEventServiceTest {

    @Mock
    private TripEventRepository tripEventRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private TripEventMapper tripEventMapper;

    @Mock
    private UserTripService userTripService;

    @InjectMocks
    private TripEventService tripEventService;

    private AutoCloseable closeable;
    private Trip trip;
    private TripEvent event;
    private TripEventDTO eventDTO;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        trip = createTestTrip();
        event = createTestTripEvent(trip);
        eventDTO = createTestTripEventDTO();
    }

    private Trip createTestTrip() {
        Trip trip = new Trip();
        trip.setTitle("Business Trip");
        return trip;
    }

    private TripEvent createTestTripEvent(Trip trip) {
        TripEvent event = new TripEvent();
        event.setTitle("Dinner");
        event.setEventDescription("Evening dinner with team");
        event.setEventDate(LocalDateTime.now().plusDays(1));
        event.setTrip(trip);
        return event;
    }

    private TripEventDTO createTestTripEventDTO() {
        return new TripEventDTO(null, "Dinner", "Evening dinner with team", event.getEventDate());
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void givenTripWithEvents_whenGetEventsByTripId_thenReturnEventList() {
        when(tripEventRepository.findByTripId(1)).thenReturn(List.of(event));
        when(tripEventMapper.toDTO(event)).thenReturn(eventDTO);

        List<TripEventDTO> events = tripEventService.getEventsByTripId(1);

        assertNotNull(events);
        assertEquals(1, events.size());
        assertEquals("Dinner", events.get(0).title());
    }

    @Test
    void givenValidTrip_whenCreateEvent_thenReturnCreatedEvent() {
        TripEvent unsavedEvent = createTestTripEvent(trip);
        TripEvent savedEvent = createTestTripEvent(trip);
        ReflectionTestUtils.setField(savedEvent, "id", 1);

        when(tripRepository.findById(1)).thenReturn(Optional.of(trip));
        when(tripEventMapper.toEntity(eventDTO)).thenReturn(unsavedEvent);
        when(tripEventRepository.save(any(TripEvent.class))).thenReturn(savedEvent);
        when(userTripService.isCurrentUserOwner(1)).thenReturn(true);
        TripEventDTO savedEventDTO = new TripEventDTO(1, savedEvent.getTitle(), savedEvent.getEventDescription(), savedEvent.getEventDate());
        when(tripEventMapper.toDTO(savedEvent)).thenReturn(savedEventDTO);

        TripEventDTO created = tripEventService.createEvent(1, eventDTO);

        assertNotNull(created, "Created event should not be null");
        assertEquals(1, created.id());
        assertEquals("Dinner", created.title());

        ArgumentCaptor<TripEvent> captor = ArgumentCaptor.forClass(TripEvent.class);
        verify(tripEventRepository).save(captor.capture());
        TripEvent capturedEvent = captor.getValue();
        assertNotNull(capturedEvent.getTrip(), "Trip should not be null");
        assertEquals("Dinner", capturedEvent.getTitle(), "Title should match");
    }
}