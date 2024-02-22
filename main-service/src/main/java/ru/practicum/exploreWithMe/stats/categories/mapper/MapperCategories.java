package ru.practicum.exploreWithMe.stats.categories.mapper;


import ru.practicum.exploreWithMe.stats.categories.dto.CategoryDto;
import ru.practicum.exploreWithMe.stats.categories.dto.RequestCategoriesDto;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;

public class MapperCategories {
    public static Categories toCategories(RequestCategoriesDto request) {
        return Categories.builder()
                .name(request.getName())
                .build();
    }
    public static CategoryDto toCategoryDto(Categories categories) {
        return CategoryDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }
}
