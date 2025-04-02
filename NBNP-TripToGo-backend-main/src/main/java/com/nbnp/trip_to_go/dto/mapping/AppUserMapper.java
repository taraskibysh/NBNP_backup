package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.dto.ExpenseDTO;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserDTO toDTO(AppUser appUser);
    AppUser toEntity(AppUserDTO appUserDTO);
}
