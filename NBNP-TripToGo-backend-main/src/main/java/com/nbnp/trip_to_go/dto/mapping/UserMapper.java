package com.nbnp.trip_to_go.dto.mapping;

import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.model.AppUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarImage", target = "avatarImage")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "messengerLink", target = "messengerLink")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Named("toDTO")
    AppUserDTO toDTO(AppUser user);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "avatarImage", target = "avatarImage")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "messengerLink", target = "messengerLink")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Named("toEntity")
    AppUser toEntity(AppUserDTO dto);



}
