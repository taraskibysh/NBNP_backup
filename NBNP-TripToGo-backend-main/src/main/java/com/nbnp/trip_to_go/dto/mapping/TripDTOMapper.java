package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.model.Trip;
import com.nbnp.trip_to_go.dto.TripDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripDTOMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "isActive", target = "isActive")
    Trip toTrip(TripDTO tripDTO);
    @Mapping(source = "id", target = "id")
    @Mapping(source = "isActive", target = "isActive")
    TripDTO toTripDTO(Trip trip);
}
