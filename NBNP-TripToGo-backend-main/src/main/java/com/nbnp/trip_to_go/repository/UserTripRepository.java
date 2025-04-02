package com.nbnp.trip_to_go.repository;

import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.UserTrip;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface UserTripRepository extends JpaRepository<UserTrip, Integer> {
    List<UserTrip> findByTripId(int tripId);
    void deleteByTripIdAndUserId(int tripId, int userId);
    Optional<UserTrip> findByTripIdAndUserId(int tripId, int userId);
    boolean existsByTripIdAndUserId(int tripId, int userId);
    Optional<UserTrip> findFirstByTripIdOrderByIdAsc(int tripId);
}
