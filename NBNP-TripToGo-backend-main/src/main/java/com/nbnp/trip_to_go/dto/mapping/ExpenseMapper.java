package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.dto.ExpenseDTO;
import com.nbnp.trip_to_go.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {
    @Mapping(source = "description", target = "description")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "paymentDate", target = "paymentDate")
    ExpenseDTO toDTO(Expense expense);

    @Mapping(source = "description", target = "description")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "paymentDate", target = "paymentDate")

    Expense toEntity(ExpenseDTO dto);
}
