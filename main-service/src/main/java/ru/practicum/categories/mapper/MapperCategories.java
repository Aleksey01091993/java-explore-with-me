package ru.practicum.categories.mapper;

import ru.practicum.categories.model.Categories;
import ru.practicum.categories.dto.RequestCategoriesDto;
import ru.practicum.categories.dto.ResponseCategoriesDto;

public class MapperCategories {
    public static Categories toCategories(RequestCategoriesDto request) {
        return Categories.builder()
                .name(request.getName())
                .build();
    }
    public static ResponseCategoriesDto toResponse(Categories categories) {
        return ResponseCategoriesDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }
}
