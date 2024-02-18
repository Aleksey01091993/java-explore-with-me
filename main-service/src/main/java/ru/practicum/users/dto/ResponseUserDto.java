package ru.practicum.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseUserDto {
    private Long id;
    private String name;
    private String email;
}
