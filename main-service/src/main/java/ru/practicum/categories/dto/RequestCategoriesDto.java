package ru.practicum.categories.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCategoriesDto {
    private String name;
}
