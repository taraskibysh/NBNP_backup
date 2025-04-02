package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.dto.TripDTO;
import com.nbnp.trip_to_go.dto.mapping.TripDTOMapper;
import com.nbnp.trip_to_go.model.Trip;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collections;

class TripDTOMapperTest {

    private final TripDTOMapper mapper = Mappers.getMapper(TripDTOMapper.class);

    @Test
    void testToTrip_shouldReturnCorrectTrip() {
        TripDTO tripDTO = new TripDTO(1, "Trip Title", "Description", null,"groupLink", "financeLink",
                LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true, 5L);
        Trip trip = mapper.toTrip(tripDTO);

        assertNotNull(trip);
        assertEquals(tripDTO.id(), trip.getId());
        assertEquals(tripDTO.title(), trip.getTitle());
        assertEquals(tripDTO.tripDescription(), trip.getTripDescription());
        assertEquals(tripDTO.groupLink(), trip.getGroupLink());
        assertEquals(tripDTO.financeLink(), trip.getFinanceLink());
        assertEquals(tripDTO.startDate(), trip.getStartDate());
        assertEquals(tripDTO.endDate(), trip.getEndDate());
        assertEquals(tripDTO.isActive(), trip.getIsActive());
    }

    @Test
    void testToTripDTO_shouldReturnCorrectTripDTO() {
        Trip trip = new Trip(2, "Trip Title2", "Description2", null, "groupLink2", "financeLink2",
                LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4), true, Collections.emptyList());

        TripDTO tripDTO = mapper.toTripDTO(trip);

        assertNotNull(tripDTO);
        assertEquals(trip.getId(), tripDTO.id());
        assertEquals(trip.getTitle(), tripDTO.title());
        assertEquals(trip.getTripDescription(), tripDTO.tripDescription());
        assertEquals(trip.getGroupLink(), tripDTO.groupLink());
        assertEquals(trip.getFinanceLink(), tripDTO.financeLink());
        assertEquals(trip.getStartDate(), tripDTO.startDate());
        assertEquals(trip.getEndDate(), tripDTO.endDate());
        assertEquals(trip.getIsActive(), tripDTO.isActive());
    }
}
