package com.nbnp.trip_to_go.unit;

import com.nbnp.trip_to_go.dto.AppUserDTO;
import com.nbnp.trip_to_go.dto.mapping.AppUserMapper;
import com.nbnp.trip_to_go.dto.mapping.UserMapper;
import com.nbnp.trip_to_go.model.AppUser;
import com.nbnp.trip_to_go.model.Sex;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserMapperTest {

    private final AppUserMapper userMapper = Mappers.getMapper(AppUserMapper.class);

    @Test
    public void testToDTO() {
        AppUser user = new AppUser();
        user.setFullName("John Doe");
        user.setAvatarImage("avatar.jpg");
        user.setEmail("john.doe@gmail.com");
        user.setSex(Sex.male);
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setMessengerLink("http://messenger.com/johndoe");
        user.setPhoneNumber("1234567890");

        AppUserDTO dto = userMapper.toDTO(user);
        assertNotNull(dto);
        assertEquals("John Doe", dto.fullName());
        assertEquals("avatar.jpg", dto.avatarImage());
        assertEquals("john.doe@gmail.com", dto.email());
        assertEquals(Sex.male, dto.sex());
        assertEquals(LocalDate.of(1990, 1, 1), dto.dateOfBirth());
        assertEquals("http://messenger.com/johndoe", dto.messengerLink());
        assertEquals("1234567890", dto.phoneNumber());
    }

    @Test
    public void testToEntity() {
        AppUserDTO dto = new AppUserDTO( 1,"John Doe", "johndoe@example.com", "avatar.jpg", Sex.male,
                LocalDate.of(1990, 1, 1), "http://messenger.com/johndoe", "1234567890");
        AppUser user = userMapper.toEntity(dto);
        assertNotNull(user);
        assertEquals("John Doe", user.getFullName());
        assertEquals("avatar.jpg", user.getAvatarImage());
        assertEquals(Sex.male, user.getSex());
        assertEquals(LocalDate.of(1990, 1, 1), user.getDateOfBirth());
        assertEquals("http://messenger.com/johndoe", user.getMessengerLink());
        assertEquals("1234567890", user.getPhoneNumber());
    }
}
