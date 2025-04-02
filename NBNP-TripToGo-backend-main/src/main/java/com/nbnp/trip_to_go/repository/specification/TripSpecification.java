package com.nbnp.trip_to_go.repository.specification;

import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.model.UserTrip;
import com.nbnp.trip_to_go.dto.TripFilterDTO;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class TripSpecification {

    public static Specification<Trip> filterByUser(TripFilterDTO filter, Integer userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) return criteriaBuilder.conjunction();

            Join<Trip, UserTrip> userTripJoin = root.join("userTrips");

            if (filter.myTrips() != null) {
                return criteriaBuilder.and(
                        criteriaBuilder.equal(userTripJoin.get("user").get("id"), userId),
                        criteriaBuilder.equal(userTripJoin.get("isOwner"), filter.myTrips())
                );
            }

            return criteriaBuilder.equal(userTripJoin.get("user").get("id"), userId);
        };
    }

    public static Specification<Trip> filterByActive(TripFilterDTO filter) {
        return (root, query, criteriaBuilder) ->
                filter.isActive() == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isActive"), filter.isActive());
    }

    public static Specification<Trip> filterByMinStartDate(TripFilterDTO filter) {
        return (root, query, criteriaBuilder) ->
                filter.minStartDate() == null ? criteriaBuilder.conjunction() : criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), filter.minStartDate());
    }

    public static Specification<Trip> applyFilters(TripFilterDTO filter, Integer userId) {
        return (root, query, criteriaBuilder) -> {
            Specification<Trip> spec = Specification.where(filterByUser(filter, userId));

            if (filter.isActive() != null) {
                spec = spec.and(filterByActive(filter));
            }
            if (filter.minStartDate() != null) {
                spec = spec.and(filterByMinStartDate(filter));
            }

            query.orderBy(criteriaBuilder.asc(root.get("startDate")));

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
