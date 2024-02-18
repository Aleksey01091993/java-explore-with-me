package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestUserDto {
    private String name;
    private String email;
}
