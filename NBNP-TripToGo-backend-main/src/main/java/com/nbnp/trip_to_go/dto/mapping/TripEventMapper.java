package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.dto.TripEventDTO;
import com.nbnp.trip_to_go.model.TripEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TripEventMapper {
    @Mapping(source = "title", target = "title")
    @Mapping(source = "eventDescription", target = "eventDescription")
    @Mapping(source = "eventDate", target = "eventDate")
    TripEventDTO toDTO(TripEvent event);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "eventDescription", target = "eventDescription")
    @Mapping(source = "eventDate", target = "eventDate")
    TripEvent toEntity(TripEventDTO dto);
}
