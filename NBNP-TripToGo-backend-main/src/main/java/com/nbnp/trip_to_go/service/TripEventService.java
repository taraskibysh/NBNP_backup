package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.dto.TripEventDTO;
import com.nbnp.trip_to_go.dto.mapping.TripEventMapper;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.model.TripEvent;
import com.nbnp.trip_to_go.repository.TripEventRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TripEventService {

    private final TripEventRepository tripEventRepository;
    private final TripRepository tripRepository;
    private final TripEventMapper tripEventMapper;
    private final UserTripService userTripService;

    public TripEventService(TripEventRepository tripEventRepository, TripRepository tripRepository, TripEventMapper tripEventMapper, UserTripService userTripService) {
        this.tripEventRepository = tripEventRepository;
        this.tripRepository = tripRepository;
        this.tripEventMapper = tripEventMapper;
        this.userTripService = userTripService;
    }

    public List<TripEventDTO> getEventsByTripId(int tripId) {
        return tripEventRepository.findByTripId(tripId).stream()
                .map(tripEventMapper::toDTO)
                .toList();
    }

    public TripEventDTO getEventById(int tripId, int eventId) {
        TripEvent event = getTripEvent(tripId, eventId);
        return tripEventMapper.toDTO(event);
    }

    @Transactional
    public TripEventDTO createEvent(int tripId, TripEventDTO eventDTO) {
        if (!userTripService.isCurrentUserOwner(tripId)){
            throw new AccessDeniedException("User has no rights to create events");
        }
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        TripEvent event = tripEventMapper.toEntity(eventDTO);
        event.setTrip(trip);

        TripEvent savedEvent = tripEventRepository.save(event);
        return tripEventMapper.toDTO(savedEvent);
    }


    @Transactional
    public TripEventDTO updateEvent(int tripId, int eventId, TripEventDTO eventDTO) {
        if (!userTripService.isCurrentUserOwner(tripId)){
            throw new AccessDeniedException("User has no rights to update events");
        }
        TripEvent existingEvent = getTripEvent(tripId, eventId);

        existingEvent.setTitle(eventDTO.title());
        existingEvent.setEventDescription(eventDTO.eventDescription());
        existingEvent.setEventDate(eventDTO.eventDate());

        TripEvent updatedEvent = tripEventRepository.save(existingEvent);
        return tripEventMapper.toDTO(updatedEvent);
    }


    @Transactional
    public void deleteEvent(int tripId, int eventId) {
        if (!userTripService.isCurrentUserOwner(tripId)){
            throw new AccessDeniedException("User has no rights to delete events");
        }
        TripEvent event = getTripEvent(tripId, eventId);
        tripEventRepository.deleteById(event.getId());
    }


    private TripEvent getTripEvent(int tripId, int eventId) {
        TripEvent event = tripEventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id " + eventId));

        if (!event.getTrip().getId().equals(tripId)) {
            throw new EntityNotFoundException("Event with id " + eventId + " does not belong to trip " + tripId);
        }

        return event;
    }
}
