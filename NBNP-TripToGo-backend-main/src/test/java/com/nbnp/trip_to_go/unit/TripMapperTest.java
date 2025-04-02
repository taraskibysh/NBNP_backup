package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.dto.TripDTO;
import com.nbnp.trip_to_go.dto.mapping.TripMapper;
import com.nbnp.trip_to_go.model.Trip;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TripMapperTest {

    private final TripMapper tripMapper = Mappers.getMapper(TripMapper.class);

    @Test
    public void testToDTO() {
        Trip trip = new Trip();
        trip.setTitle("Hiking");
        trip.setTripDescription("Mountain hiking");
        trip.setGroupLink("link");
        trip.setFinanceLink("finance");
        trip.setStartDate(LocalDateTime.of(2025, 5, 1, 9, 0));
        trip.setEndDate(LocalDateTime.of(2025, 5, 10, 18, 0));
        trip.setIsActive(true);

        TripDTO dto = tripMapper.toDTO(trip);
        assertNotNull(dto);
        assertEquals("Hiking", dto.title());
        assertEquals("Mountain hiking", dto.tripDescription());
        assertEquals("link", dto.groupLink());
        assertEquals("finance", dto.financeLink());
        assertEquals(LocalDateTime.of(2025, 5, 1, 9, 0), dto.startDate());
        assertEquals(LocalDateTime.of(2025, 5, 10, 18, 0), dto.endDate());
        assertTrue(dto.isActive());
    }

    @Test
    public void testToEntity() {
        TripDTO dto = new TripDTO(
                null,
                "Hiking",
                "Mountain hiking",
                null,
                "link",
                "finance",
                LocalDateTime.of(2025, 5, 1, 9, 0),
                LocalDateTime.of(2025, 5, 10, 18, 0),
                true,
                null
        );

        Trip trip = tripMapper.toEntity(dto);
        assertNotNull(trip);
        assertEquals("Hiking", trip.getTitle());
        assertEquals("Mountain hiking", trip.getTripDescription());
        assertEquals("link", trip.getGroupLink());
        assertEquals("finance", trip.getFinanceLink());
        assertEquals(LocalDateTime.of(2025, 5, 1, 9, 0), trip.getStartDate());
        assertEquals(LocalDateTime.of(2025, 5, 10, 18, 0), trip.getEndDate());
        assertTrue(trip.getIsActive());
    }
}
