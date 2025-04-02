package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.config.JwtService;
import com.nbnp.trip_to_go.dto.*;
import com.nbnp.trip_to_go.dto.mapping.*;
import com.nbnp.trip_to_go.exception.UserNotFoundException;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.model.UserTrip;
import com.nbnp.trip_to_go.repository.*;
import com.nbnp.trip_to_go.repository.projection.TripParticipantsCountProjection;
import com.nbnp.trip_to_go.repository.specification.TripSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TaskRepository taskRepository;
    private final TripEventRepository tripEventRepository;
    private final ExpenseRepository expenseRepository;
    private final UserTripRepository userTripRepository;
    private final TripMapper tripMapper;
    private final TaskMapper taskMapper;
    private final TripEventMapper tripEventMapper;
    private final ExpenseMapper expenseMapper;
    private final UserMapper userMapper;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public TripService(TripRepository tripRepository,
                       TaskRepository taskRepository,
                       TripEventRepository tripEventRepository,
                       ExpenseRepository expenseRepository,
                       UserTripRepository userTripRepository,
                       TripMapper tripMapper,
                       TaskMapper taskMapper,
                       TripEventMapper tripEventMapper,
                       ExpenseMapper expenseMapper,
                       UserMapper userMapper, AppUserRepository appUserRepository, JwtService jwtService) {
        this.tripRepository = tripRepository;
        this.taskRepository = taskRepository;
        this.tripEventRepository = tripEventRepository;
        this.expenseRepository = expenseRepository;
        this.userTripRepository = userTripRepository;
        this.tripMapper = tripMapper;
        this.taskMapper = taskMapper;
        this.tripEventMapper = tripEventMapper;
        this.expenseMapper = expenseMapper;
        this.userMapper = userMapper;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    public List<Trip> getAllTrips(TripFilterDTO filter) {
        String userEmail = jwtService.getCurrentUsername();
        AppUser currentUser = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email " + userEmail));

        Specification<Trip> spec = TripSpecification.applyFilters(filter, currentUser.getId());
        return tripRepository.findAll(spec);
    }

    public Map<Integer, Long> countParticipantsById(List<Integer> tripIds) {
        if (tripIds == null) {
            throw new IllegalArgumentException("Trip IDs cannot be null");
        }
        List<TripParticipantsCountProjection> counts = tripRepository.countParticipantsByTripIds(tripIds);
        return counts.stream()
                .collect(Collectors.toMap(
                        TripParticipantsCountProjection::getTripId,
                        TripParticipantsCountProjection::getParticipantCount
                ));
    }

    public TripDTO createTrip(TripDTO tripDTO) {
        validateTrip(tripDTO);
        Trip trip = tripMapper.toEntity(tripDTO);
        trip = tripRepository.save(trip);

        AppUser currentUser = appUserRepository.findByEmail(jwtService.getCurrentUsername())
                .orElseThrow(() -> new UserNotFoundException(jwtService.getCurrentUsername()));

        UserTrip userTrip = new UserTrip();
        userTrip.setTrip(trip);
        userTrip.setUser(currentUser);
        userTrip.setOwner(true);
        userTripRepository.save(userTrip);

        return tripMapper.toDTO(trip);
    }

    public TripDetailsDTO getTripDetails(int tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        TripDTO tripDTO = tripMapper.toDTO(trip);

        List<AppUserDTO> participants = userTripRepository.findByTripId(tripId).stream()
                .map(userTrip -> userMapper.toDTO(userTrip.getUser()))
                .collect(Collectors.toList());

        List<TripEventDTO> events = tripEventRepository.findByTripId(tripId).stream()
                .map(tripEventMapper::toDTO)
                .collect(Collectors.toList());

        List<TaskDTO> tasks = taskRepository.findByTripId(tripId).stream()
                .map(taskMapper::toDTO)
                .collect(Collectors.toList());

        List<ExpenseDTO> expenses = expenseRepository.findByTripId(tripId).stream()
                .map(expenseMapper::toDTO)
                .collect(Collectors.toList());

        return new TripDetailsDTO(tripDTO, participants, events, tasks, expenses);
    }

    @Transactional
    public TripDTO updateTrip(int tripId, TripDTO tripDTO) {
        validateTrip(tripDTO);

        Trip existingTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        existingTrip.setId(tripId);
        existingTrip.setTitle(tripDTO.title());
        existingTrip.setTripDescription(tripDTO.tripDescription());
        existingTrip.setAvatarImage(tripDTO.avatarImage());
        existingTrip.setGroupLink(tripDTO.groupLink());
        existingTrip.setFinanceLink(tripDTO.financeLink());
        existingTrip.setStartDate(tripDTO.startDate());
        existingTrip.setEndDate(tripDTO.endDate());
        existingTrip.setIsActive(Boolean.TRUE.equals(tripDTO.isActive()));

        Trip updatedTrip = tripRepository.save(existingTrip);
        return tripMapper.toDTO(updatedTrip);
    }

    @Transactional
    public TripDTO partiallyUpdateTrip(int tripId, TripDTO tripDTO) {
        Trip existingTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        if (tripDTO.title() != null && !tripDTO.title().isEmpty()) {
            existingTrip.setTitle(tripDTO.title());
        }
        if (tripDTO.tripDescription() != null && !tripDTO.tripDescription().isEmpty()) {
            existingTrip.setTripDescription(tripDTO.tripDescription());
        }
        if (tripDTO.groupLink() != null && !tripDTO.groupLink().isEmpty()) {
            existingTrip.setGroupLink(tripDTO.groupLink());
        }
        if (tripDTO.financeLink() != null && !tripDTO.financeLink().isEmpty()) {
            existingTrip.setFinanceLink(tripDTO.financeLink());
        }
        if (tripDTO.startDate() != null) {
            existingTrip.setStartDate(tripDTO.startDate());
        }
        if (tripDTO.endDate() != null) {
            existingTrip.setEndDate(tripDTO.endDate());
        }
        if (tripDTO.endDate() != null) {
            existingTrip.setAvatarImage(tripDTO.avatarImage());
        }
        if (tripDTO.isActive() != null) {
            existingTrip.setIsActive(Boolean.TRUE.equals(tripDTO.isActive()));
        }

        if (existingTrip.getStartDate() != null && existingTrip.getEndDate() != null &&
                existingTrip.getEndDate().isBefore(existingTrip.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }

        existingTrip = tripRepository.save(existingTrip);
        return tripMapper.toDTO(existingTrip);
    }

    @Transactional
    public void deleteTrip(int tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        trip.setIsActive(false);
        tripRepository.save(trip);
    }

    private void validateTrip(TripDTO tripDTO) {
        if (tripDTO.title() == null || tripDTO.title().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
    }

    public TripDTO getTrip(int tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        return tripMapper.toDTO(trip);
    }
}