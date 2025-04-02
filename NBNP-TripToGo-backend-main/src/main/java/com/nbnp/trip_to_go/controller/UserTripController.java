package com.nbnp.trip_to_go.controller;

import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.dto.mapping.UserMapper;
import com.nbnp.trip_to_go.model.UserTrip;
import com.nbnp.trip_to_go.service.UserTripService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trips/{tripId}/participants")
public class UserTripController {

    private final UserTripService userTripService;
    private final UserMapper userMapper;

    public UserTripController(UserTripService userTripService, UserMapper userMapper) {
        this.userTripService = userTripService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<AppUserDTO> getTripParticipants(@PathVariable int tripId) {
        List<UserTrip> userTrips = userTripService.getParticipantsByTripId(tripId);
        return userTrips.stream()
                .map(ut -> userMapper.toDTO(ut.getUser()))
                .collect(Collectors.toList());
    }

    @PostMapping("/join")
    public ResponseEntity<Void> joinTrip(@PathVariable int tripId) {
        userTripService.joinTrip(tripId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/leave")
    public ResponseEntity<Void> leaveTrip(@PathVariable int tripId) {
        userTripService.leaveTrip(tripId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/promote")
    public ResponseEntity<Void> promoteUserToOwner(@PathVariable int tripId, @PathVariable int userId) {
        userTripService.promoteUserToOwner(tripId, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{userId}/kick")
    public ResponseEntity<Void> kickUser(@PathVariable int tripId, @PathVariable int userId) {
        userTripService.kickUserFromTrip(tripId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/is-owner")
    public ResponseEntity<Boolean> isOwner(@PathVariable int tripId) {
        Boolean isOwner = userTripService.isCurrentUserOwner(tripId);
        return ResponseEntity.ok(isOwner);

    }
}
