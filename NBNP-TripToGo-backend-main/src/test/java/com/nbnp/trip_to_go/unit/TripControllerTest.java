package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.controller.TripController;
import com.nbnp.trip_to_go.dto.TripDTO;
import com.nbnp.trip_to_go.dto.TripFilterDTO;
import com.nbnp.trip_to_go.dto.mapping.TripDTOMapper;
import com.nbnp.trip_to_go.exception.response.ErrorResponse;
import com.nbnp.trip_to_go.exception.handler.GlobalExceptionHandler;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.service.TripService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TripControllerTest {

    @InjectMocks
    private TripController tripController;

    @Mock
    private TripService tripService;

    @Mock
    private TripDTOMapper tripDTOMapper;

    @Mock
    private WebRequest request;

    @Test
    void getAllTrips_ShouldReturnTripDTOList() {
        // Arrange
        LocalDateTime testStartDate = LocalDateTime.of(2025, 1, 1, 12, 0);
        LocalDateTime testEndDate = LocalDateTime.of(2025, 1, 2, 12, 0);

        Trip trip1 = new Trip();
        trip1.setId(1);
        trip1.setTitle("Trip One");
        trip1.setTripDescription("Description One");
        trip1.setAvatarImage("avatar one");
        trip1.setGroupLink("groupLinkOne");
        trip1.setFinanceLink("financeLinkOne");
        trip1.setStartDate(testStartDate);
        trip1.setEndDate(testEndDate);
        trip1.setIsActive(true);

        Trip trip2 = new Trip();
        trip2.setId(2);
        trip2.setTitle("Trip Two");
        trip2.setTripDescription("Description Two");
        trip2.setAvatarImage("avatar two");
        trip2.setGroupLink("groupLinkTwo");
        trip2.setFinanceLink("financeLinkTwo");
        trip2.setStartDate(testStartDate);
        trip2.setEndDate(testEndDate);
        trip2.setIsActive(false);

        TripFilterDTO filter = new TripFilterDTO(true, true, LocalDateTime.now());

        when(tripService.getAllTrips(filter)).thenReturn(List.of(trip1, trip2));
        when(tripService.countParticipantsById(List.of(1, 2))).thenReturn(Map.of(1, 10L, 2, 20L));
        when(tripDTOMapper.toTripDTO(trip1)).thenReturn(new TripDTO(1, "Trip One", "Description One", "avatar one", "groupLinkOne", "financeLinkOne", trip1.getStartDate(), trip1.getEndDate(), true, 10L));
        when(tripDTOMapper.toTripDTO(trip2)).thenReturn(new TripDTO(2, "Trip Two", "Description Two", "avatar two", "groupLinkTwo","financeLinkTwo", trip2.getStartDate(), trip2.getEndDate(), false,  20L));


        List<TripDTO> result = tripController.getAllTrips(filter);

        assertEquals(2, result.size());

        TripDTO dto1 = result.get(0);
        assertEquals(1, dto1.id());
        assertEquals("Trip One", dto1.title());
        assertEquals("Description One", dto1.tripDescription());
        assertEquals("avatar one", dto1.avatarImage());
        assertEquals("groupLinkOne", dto1.groupLink());
        assertEquals("financeLinkOne", dto1.financeLink());
        assertEquals(true, dto1.isActive());
        assertEquals(10L, dto1.participantsCount());
        assertNotNull(dto1.startDate());
        assertNotNull(dto1.endDate());

        TripDTO dto2 = result.get(1);
        assertEquals(2, dto2.id());
        assertEquals("Trip Two", dto2.title());
        assertEquals("Description Two", dto2.tripDescription());
        assertEquals("avatar two", dto2.avatarImage());
        assertEquals("groupLinkTwo", dto2.groupLink());
        assertEquals("financeLinkTwo", dto2.financeLink());
        assertEquals(false, dto2.isActive());
        assertEquals(20L, dto2.participantsCount());
        assertNotNull(dto2.startDate());
        assertNotNull(dto2.endDate());
    }

    @Test
    void getAllTrips_whenUserIdInFilterDTOIsNull_ShouldHandleIllegalArgumentException() {
        TripFilterDTO filter = new TripFilterDTO(null, null, null);
        when(tripService.getAllTrips(any(TripFilterDTO.class))).thenThrow(new IllegalArgumentException("User must be specified"));
        when(request.getDescription(false)).thenReturn("uri=/api/trips");

        // Handle exception through the controller advice method
        ErrorResponse response = null;
        try {
            tripController.getAllTrips(filter);
        } catch (IllegalArgumentException ex) {
            response = new GlobalExceptionHandler().handleIllegalArgumentException(ex, request);
        }

        // Assertions
        assertNotNull(response);
        assertEquals("Bad Request", response.error());
        assertEquals("User must be specified", response.message());
        assertEquals("/api/trips", response.path());
    }

    @Test
    void getAllTrips_whenSomeInternalError_ShouldHandleGenericException() {
        TripFilterDTO filter = new TripFilterDTO(true, true, LocalDateTime.now());
        when(tripService.getAllTrips(any())).thenThrow(new RuntimeException("Unexpected failure"));
        when(request.getDescription(false)).thenReturn("uri=/api/trips");

        // Handle exception through the controller advice method
        ErrorResponse response = null;
        try {
            tripController.getAllTrips(filter);
        } catch (Exception ex) {
            response = new GlobalExceptionHandler().handleGenericException(ex, request);
        }

        // Assertions
        assertNotNull(response);
        assertEquals("Internal Server Error", response.error());
        assertEquals("Unexpected error: Unexpected failure", response.message());
        assertEquals("/api/trips", response.path());
    }

}