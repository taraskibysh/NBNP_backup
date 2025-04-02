package com.nbnp.trip_to_go.controller;

import com.nbnp.trip_to_go.model.Expense;
import com.nbnp.trip_to_go.service.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips/{tripId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getTripExpenses(@PathVariable int tripId) {
        return expenseService.getExpensesByTripId(tripId);
    }

    @PostMapping
    public Expense addExpense(@PathVariable int tripId, @RequestBody Expense expense) {
        return expenseService.addExpense(tripId, expense);
    }

    @DeleteMapping("/{expenseId}")
    public void deleteExpense(@PathVariable int tripId, @PathVariable int expenseId) {
        expenseService.deleteExpense(tripId, expenseId);
    }

}
