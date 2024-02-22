package ru.practicum.exploreWithMe.stats.categories.mapper;


import ru.practicum.exploreWithMe.stats.categories.dto.CategoryDto;
import ru.practicum.exploreWithMe.stats.categories.dto.NewCategoryDto;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;

public class MapperCategories {
    public static Categories toCategories(NewCategoryDto request) {
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
