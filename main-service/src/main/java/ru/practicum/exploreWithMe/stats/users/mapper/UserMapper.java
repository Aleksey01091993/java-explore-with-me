package ru.practicum.exploreWithMe.stats.users.mapper;


import ru.practicum.exploreWithMe.stats.users.dto.RequestUserDto;
import ru.practicum.exploreWithMe.stats.users.dto.ResponseUserDto;
import ru.practicum.exploreWithMe.stats.users.dto.UserShortDto;
import ru.practicum.exploreWithMe.stats.users.model.User;

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
    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
