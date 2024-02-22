package ru.practicum.exploreWithMe.stats.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.categories.dto.CategoryDto;
import ru.practicum.exploreWithMe.stats.categories.dto.RequestCategoriesDto;
import ru.practicum.exploreWithMe.stats.categories.mapper.MapperCategories;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.categories.repository.CategoriesRepository;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository repository;

    public CategoryDto add(RequestCategoriesDto request) {
        return MapperCategories.toCategoryDto(repository.save(MapperCategories.toCategories(request)));
    }

    public CategoryDto delete(Long categoriesId) {
        Categories categories = repository.findById(categoriesId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + categoriesId));
        repository.delete(categories);
        return MapperCategories.toCategoryDto(categories);
    }

    public CategoryDto update(RequestCategoriesDto request, Long categoriesId) {
        Categories categories = repository.findById(categoriesId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + categoriesId));
        return MapperCategories.toCategoryDto(repository.save(MapperCategories.toCategories(request)));
    }

    public CategoryDto get(Long categoriesId) {
        return MapperCategories.toCategoryDto(repository.findById(categoriesId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + categoriesId)));
    }

    public List<CategoryDto> getAll() {
        return repository.findAll().stream()
                .map(MapperCategories::toCategoryDto)
                .collect(Collectors.toList());
    }
}
