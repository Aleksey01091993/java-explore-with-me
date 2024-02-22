package ru.practicum.exploreWithMe.stats.categories.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class NewCategoryDto {
    @NotNull
    @NotBlank
    @NotEmpty
    private String name;
}
