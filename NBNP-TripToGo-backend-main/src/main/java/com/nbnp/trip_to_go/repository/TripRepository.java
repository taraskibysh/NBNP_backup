package com.nbnp.trip_to_go.repository;

import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.repository.projection.TripParticipantsCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer>, JpaSpecificationExecutor<Trip> {

    @Query("SELECT ut.trip.id as tripId, COUNT(ut) as participantCount " +
            "FROM UserTrip ut " +
            "WHERE ut.trip.id IN :tripIds " +
            "GROUP BY ut.trip.id")
    List<TripParticipantsCountProjection> countParticipantsByTripIds(@Param("tripIds") List<Integer> tripIds);
    List<Trip> findTop3ByOrderByStartDateDesc();
}
