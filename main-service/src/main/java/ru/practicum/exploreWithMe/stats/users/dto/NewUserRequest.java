package ru.practicum.exploreWithMe.stats.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewUserRequest {
    private String email;
    private String name;
}
