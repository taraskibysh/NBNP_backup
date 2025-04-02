package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.dto.ExpenseDTO;
import com.nbnp.trip_to_go.dto.mapping.ExpenseMapper;
import com.nbnp.trip_to_go.model.Expense;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseMapperTest {

    private final ExpenseMapper expenseMapper = Mappers.getMapper(ExpenseMapper.class);

    @Test
    public void testToDTO() {
        Expense expense = new Expense();
        expense.setDescription("Hotel booking");
        expense.setAmount(BigDecimal.valueOf(500));
        expense.setPaymentDate(LocalDateTime.of(2025, 2, 26, 12, 0));

        ExpenseDTO dto = expenseMapper.toDTO(expense);
        assertNotNull(dto);
        assertEquals("Hotel booking", dto.description());
        assertEquals(BigDecimal.valueOf(500), dto.amount());
        assertEquals(LocalDateTime.of(2025, 2, 26, 12, 0), dto.paymentDate());
    }

    @Test
    public void testToEntity() {
        ExpenseDTO dto = new ExpenseDTO("Hotel booking", BigDecimal.valueOf(500), LocalDateTime.of(2025, 2, 26, 12, 0));
        Expense expense = expenseMapper.toEntity(dto);
        assertNotNull(expense);
        assertEquals("Hotel booking", expense.getDescription());
        assertEquals(BigDecimal.valueOf(500), expense.getAmount());
        assertEquals(LocalDateTime.of(2025, 2, 26, 12, 0), expense.getPaymentDate());
    }
}
