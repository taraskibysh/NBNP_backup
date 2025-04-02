package com.nbnp.trip_to_go.repository;

import com.nbnp.trip_to_go.model.TripEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TripEventRepository extends JpaRepository<TripEvent, Integer> {
    List<TripEvent> findByTripId(int tripId);
}
