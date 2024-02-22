package ru.practicum.exploreWithMe.stats.users.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewUserRequest {
    @NotNull
    @NotBlank
    private String name;
    @Email
    private String email;
}
