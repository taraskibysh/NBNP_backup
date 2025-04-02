package com.nbnp.trip_to_go.controller;

import com.nbnp.trip_to_go.dto.TripDetailsDTO;
import com.nbnp.trip_to_go.dto.mapping.TripDTOMapper;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.dto.TripDTO;
import com.nbnp.trip_to_go.dto.TripFilterDTO;
import com.nbnp.trip_to_go.dto.TripDetailsDTO;
import com.nbnp.trip_to_go.service.TripService;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityNotFoundException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;
    private final TripDTOMapper tripDTOMapper;

    public TripController(TripService tripService, TripDTOMapper tripDTOMapper) {
        this.tripService = tripService;
        this.tripDTOMapper = tripDTOMapper;
    }

    @GetMapping
    public List<TripDTO> getAllTrips(TripFilterDTO tripFilterDTO) {

        List<Trip> trips = tripService.getAllTrips(tripFilterDTO);
        List<Integer> tripIds = trips.stream().map(Trip::getId).toList();
        if (tripIds.isEmpty()){
            return Collections.emptyList();
        }
        Map<Integer, Long> participantCounts = tripService.countParticipantsById(tripIds);

        return trips.stream()
                .map(trip -> {
                    Long participants = participantCounts.getOrDefault(trip.getId(), 0L);
                    return tripDTOMapper.toTripDTO(trip)
                            .withTripParticipants(participants);
                })
                .toList();
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<TripDTO> getTrip(@PathVariable int tripId) {
        TripDTO trip = tripService.getTrip(tripId);
        return ResponseEntity.ok(trip);
    }

    @PostMapping
    public ResponseEntity<TripDTO> createTrip(@RequestBody @Valid TripDTO tripDTO) {
        TripDTO created = tripService.createTrip(tripDTO);
        return ResponseEntity.ok(created);
    }
    @GetMapping("/{tripId}/details")
    public ResponseEntity<TripDetailsDTO> getTripDetails(@PathVariable int tripId) {
        TripDetailsDTO details = tripService.getTripDetails(tripId);
        return ResponseEntity.ok(details);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<TripDTO> updateTrip(@PathVariable int tripId, @RequestBody TripDTO tripDTO) {
        TripDTO updated = tripService.updateTrip(tripId, tripDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{tripId}")
    public ResponseEntity<TripDTO> partiallyUpdateTrip(@PathVariable int tripId, @RequestBody TripDTO tripDTO) {
        TripDTO updated = tripService.partiallyUpdateTrip(tripId, tripDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable int tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.noContent().build();
    }
}
