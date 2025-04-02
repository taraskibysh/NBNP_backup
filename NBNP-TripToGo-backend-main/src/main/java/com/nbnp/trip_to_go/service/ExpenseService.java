package com.nbnp.trip_to_go.service;

import com.nbnp.trip_to_go.model.Expense;
import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.repository.ExpenseRepository;
import com.nbnp.trip_to_go.repository.TripRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final TripRepository tripRepository;

    public ExpenseService(ExpenseRepository expenseRepository, TripRepository tripRepository) {
        this.expenseRepository = expenseRepository;
        this.tripRepository = tripRepository;
    }

    public List<Expense> getExpensesByTripId(int tripId) {
        return expenseRepository.findByTripId(tripId);
    }

    @Transactional
    public Expense addExpense(int tripId, Expense expense) {
        if (expense.getAmount() == null || expense.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found with id " + tripId));

        expense.setTrip(trip);
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(int tripId, int expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException("Expense not found with id " + expenseId));

        if (!Objects.equals(tripId, expense.getTrip().getId())) {
            throw new EntityNotFoundException("Expense with id " + expenseId + " does not belong to trip " + tripId);
        }

        expenseRepository.deleteById(expenseId);
    }
}
