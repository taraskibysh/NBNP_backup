package com.nbnp.trip_to_go.repository.specification;

import com.nbnp.trip_to_go.dto.TaskFilterDTO;
import com.nbnp.trip_to_go.model.SortType;
import com.nbnp.trip_to_go.model.Task;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

    public static Specification<Task> filterByTripId(Integer tripId) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                criteriaBuilder.equal(root.get("trip").get("id"), tripId);
    }

    public static Specification<Task> filterByMyTask(Boolean myTasks, Integer userId) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (myTasks == null) {
                return criteriaBuilder.conjunction();
            } else if (myTasks) {
                return criteriaBuilder.equal(root.get("user").get("id"), userId);
            } else {
                return criteriaBuilder.notEqual(root.get("user").get("id"), userId);
            }
        };
    }

    public static Specification<Task> filterByIsDone(Boolean isDone) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) ->
                isDone == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("isDone"), isDone);
    }

    public static Specification<Task> applySortType(SortType sortType) {
        return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (sortType == null) {
                query.orderBy(criteriaBuilder.desc(root.get("id")));
            } else if (sortType == SortType.asc) {
                query.orderBy(criteriaBuilder.asc(root.get("taskDeadline")));
            } else if (sortType == SortType.desc) {
                query.orderBy(criteriaBuilder.desc(root.get("taskDeadline")));
            }
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Task> applyFilters(TaskFilterDTO filter, Integer userId) {
        Specification<Task> specification = Specification.where(filterByTripId(filter.tripId()));
        specification = specification.and(filterByMyTask(filter.myTasks(), userId));
        specification = specification.and(filterByIsDone(filter.isDone()));
        specification = specification.and(applySortType(filter.sortType()));
        return specification;
    }
}
