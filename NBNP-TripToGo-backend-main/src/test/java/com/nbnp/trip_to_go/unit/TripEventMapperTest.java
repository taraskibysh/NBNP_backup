package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.dto.TripEventDTO;
import com.nbnp.trip_to_go.dto.mapping.TripEventMapper;
import com.nbnp.trip_to_go.model.TripEvent;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TripEventMapperTest {

    private final TripEventMapper tripEventMapper = Mappers.getMapper(TripEventMapper.class);

    @Test
    public void testToDTO() {
        TripEvent event = new TripEvent();
        event.setTitle("Dinner");
        event.setEventDescription("Team dinner");
        event.setEventDate(LocalDateTime.of(2025, 4, 10, 19, 0));

        TripEventDTO dto = tripEventMapper.toDTO(event);
        assertNotNull(dto);
        assertEquals("Dinner", dto.title());
        assertEquals("Team dinner", dto.eventDescription());
        assertEquals(LocalDateTime.of(2025, 4, 10, 19, 0), dto.eventDate());
    }

    @Test
    public void testToEntity() {
        TripEventDTO dto = new TripEventDTO(null, "Dinner", "Team dinner", LocalDateTime.of(2025, 4, 10, 19, 0));
        TripEvent event = tripEventMapper.toEntity(dto);
        assertNotNull(event);
        assertEquals("Dinner", event.getTitle());
        assertEquals("Team dinner", event.getEventDescription());
        assertEquals(LocalDateTime.of(2025, 4, 10, 19, 0), event.getEventDate());
    }
}
