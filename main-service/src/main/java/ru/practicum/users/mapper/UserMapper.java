package ru.practicum.users.mapper;

import ru.practicum.users.dto.RequestUserDto;
import ru.practicum.users.dto.ResponseUserDto;
import ru.practicum.users.model.User;

public class UserMapper {
    public static User toUser(RequestUserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static ResponseUserDto toResponse(User user) {
        return ResponseUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
