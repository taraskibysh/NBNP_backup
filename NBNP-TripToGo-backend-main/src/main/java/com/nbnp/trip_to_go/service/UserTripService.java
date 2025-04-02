package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.exception.UserAlreadyJoinedTripException;
import com.nbnp.trip_to_go.exception.UserNotFoundException;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.model.UserTrip;
import com.nbnp.trip_to_go.repository.AppUserRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import com.nbnp.trip_to_go.repository.UserTripRepository;
import com.nbnp.trip_to_go.repository.projection.TripParticipantsCountProjection;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTripService {

    private final UserTripRepository userTripRepository;
    private final TripRepository tripRepository;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public UserTripService(UserTripRepository userTripRepository,
                           TripRepository tripRepository,
                           AppUserRepository appUserRepository,
                           JwtService jwtService) {
        this.userTripRepository = userTripRepository;
        this.tripRepository = tripRepository;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    public List<UserTrip> getParticipantsByTripId(int tripId) {
        return userTripRepository.findByTripId(tripId);
    }

    private AppUser getCurrentUser() {
        return appUserRepository.findByEmail(jwtService.getCurrentUsername())
                .orElseThrow(() -> new UserNotFoundException(jwtService.getCurrentUsername()));
    }

    @Transactional
    public void joinTrip(int tripId) {
        AppUser currentUser = getCurrentUser();
        Integer userId = currentUser.getId();

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip with ID " + tripId + " does not exist"));

        if (userTripRepository.existsByTripIdAndUserId(tripId, userId)) {
            throw new UserAlreadyJoinedTripException(userId, tripId);
        }

        UserTrip userTrip = new UserTrip();
        userTrip.setTrip(trip);
        userTrip.setUser(currentUser);
        userTrip.setOwner(false);

        userTripRepository.save(userTrip);
    }

    @Transactional
    public void leaveTrip(int tripId) {
        AppUser currentUser = getCurrentUser();
        UserTrip userTrip = userTripRepository.findByTripIdAndUserId(tripId, currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User " + currentUser.getEmail() + " is not part of this trip"));

        if (isCurrentUserOwner(tripId)) {
            handleOwnerLeaving(tripId);
        }

        userTripRepository.delete(userTrip);
    }

    private void handleOwnerLeaving(int tripId) {
        long participantCount = getParticipantCount(tripId);

        if (participantCount > 1) {
            assignNewOwner(tripId);
        } else {
            deactivateTrip(tripId);
        }
    }

    private void assignNewOwner(int tripId) {
        userTripRepository.findFirstByTripIdOrderByIdAsc(tripId)
                .ifPresent(nextOwnerUserTrip -> {
                    nextOwnerUserTrip.setOwner(true);
                    userTripRepository.save(nextOwnerUserTrip);
                });
    }

    private void deactivateTrip(int tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip with ID " + tripId + " does not exist"));
        trip.setIsActive(false);
        tripRepository.save(trip);
    }

    private long getParticipantCount(int tripId) {
        return tripRepository.countParticipantsByTripIds(List.of(tripId))
                .stream()
                .collect(Collectors.toMap(
                        TripParticipantsCountProjection::getTripId,
                        TripParticipantsCountProjection::getParticipantCount
                ))
                .getOrDefault(tripId, 0L);
    }

    public void promoteUserToOwner(int tripId, int userId) {
        AppUser currentUser = appUserRepository.findByEmail(jwtService.getCurrentUsername())
                .orElseThrow(() -> new UserNotFoundException(jwtService.getCurrentUsername()));
        UserTrip currentUserTrip =  userTripRepository.findByTripIdAndUserId(tripId, currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User " + jwtService.getCurrentUsername() + " is not part of a trip or " +
                        "Trip not found with id " + tripId));

        if(!currentUserTrip.isOwner()){
            throw new AccessDeniedException("User " + currentUser.getEmail() + " has no rights to promote other users");
        }

        UserTrip promotedUserTrip = userTripRepository.findByTripIdAndUserId(tripId, userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + userId));

        promotedUserTrip.setOwner(true);
        userTripRepository.save(promotedUserTrip);
    }

    public void kickUserFromTrip(int tripId, int userId) {
        AppUser currentUser = getCurrentUser();
        UserTrip currentUserTrip =  userTripRepository.findByTripIdAndUserId(tripId, currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("User " + jwtService.getCurrentUsername() + " is not part of a trip or " +
                        "Trip not found with id " + tripId));

        if (currentUser.getId().equals(userId)) {
            throw new IllegalArgumentException("User cannot remove themselves");
        }

        if(!currentUserTrip.isOwner()){
            throw new AccessDeniedException("User " + currentUser.getEmail() + " has no rights to kick other users");
        }

        UserTrip kickedUserTrip = userTripRepository.findByTripIdAndUserId(tripId, userId)
                .orElseThrow(() -> new EntityNotFoundException("User " + currentUser.getEmail() + " is not part of this trip"));

        userTripRepository.delete(kickedUserTrip);
    }

    public boolean isCurrentUserOwner(int tripId) {
        AppUser currentUser = getCurrentUser();
        UserTrip currentUserTrip =  userTripRepository.findByTripIdAndUserId(tripId, currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));
        return currentUserTrip.isOwner();
    }
}
