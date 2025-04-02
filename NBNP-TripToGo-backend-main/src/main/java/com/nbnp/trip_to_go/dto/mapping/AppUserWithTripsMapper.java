package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.dto.AppUserWithTripsDTO;
import com.nbnp.trip_to_go.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserWithTripsMapper {
    AppUserWithTripsDTO toDTO(AppUser appUser);
    AppUser toEntity(AppUserDTO AppUserWithTripsDTO);
}
