package ru.practicum.exploreWithMe.stats.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
