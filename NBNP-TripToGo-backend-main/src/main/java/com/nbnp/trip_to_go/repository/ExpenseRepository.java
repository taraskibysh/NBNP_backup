package com.nbnp.trip_to_go.repository;

import com.nbnp.trip_to_go.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByTripId(int tripId);
}
