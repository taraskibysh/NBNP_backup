package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.dto.TripDTO;
import com.nbnp.trip_to_go.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripMapper {
    @Mapping(source = "title", target = "title")
    @Mapping(source = "tripDescription", target = "tripDescription")
    @Mapping(source = "groupLink", target = "groupLink")
    @Mapping(source = "financeLink", target = "financeLink")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "isActive", target = "isActive")
    TripDTO toDTO(Trip trip);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "tripDescription", target = "tripDescription")
    @Mapping(source = "groupLink", target = "groupLink")
    @Mapping(source = "financeLink", target = "financeLink")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "isActive", target = "isActive")
    Trip toEntity(TripDTO dto);
}
