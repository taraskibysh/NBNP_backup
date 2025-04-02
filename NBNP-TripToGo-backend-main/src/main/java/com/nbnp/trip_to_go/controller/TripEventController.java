package com.nbnp.trip_to_go.controller;

import com.nbnp.trip_to_go.dto.TripEventDTO;
import com.nbnp.trip_to_go.service.TripEventService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/events")
public class TripEventController {

    private final TripEventService tripEventService;

    public TripEventController(TripEventService tripEventService) {
        this.tripEventService = tripEventService;
    }

    @GetMapping
    public List<TripEventDTO> getTripEvents(@PathVariable int tripId) {
        return tripEventService.getEventsByTripId(tripId);
    }

    @PostMapping
    public TripEventDTO createEvent(@PathVariable int tripId, @RequestBody TripEventDTO eventDTO) {
        return tripEventService.createEvent(tripId, eventDTO);
    }

    @GetMapping("/{eventId}")
    public TripEventDTO getEventById(@PathVariable int tripId, @PathVariable int eventId) {
        return tripEventService.getEventById(tripId, eventId);
    }

    @PutMapping("/{eventId}")
    public TripEventDTO updateEvent(@PathVariable int tripId, @PathVariable int eventId, @RequestBody TripEventDTO eventDTO) {
        return tripEventService.updateEvent(tripId, eventId, eventDTO);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int tripId, @PathVariable int eventId) {
        tripEventService.deleteEvent(tripId, eventId);
        return ResponseEntity.noContent().build();
    }
}
