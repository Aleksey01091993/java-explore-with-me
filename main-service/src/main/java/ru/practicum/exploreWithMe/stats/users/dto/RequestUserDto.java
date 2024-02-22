package ru.practicum.exploreWithMe.stats.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestUserDto {
    private String name;
    private String email;
}
