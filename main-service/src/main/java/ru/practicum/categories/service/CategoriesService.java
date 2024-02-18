package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.RequestCategoriesDto;
import ru.practicum.categories.dto.ResponseCategoriesDto;
import ru.practicum.categories.mapper.MapperCategories;
import ru.practicum.categories.model.Categories;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository repository;

    public ResponseCategoriesDto add(RequestCategoriesDto request) {
        return MapperCategories.toResponse(repository.save(MapperCategories.toCategories(request)));
    }

    public ResponseCategoriesDto delete(Long categoriesId) {
        Categories categories = repository.findById(categoriesId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + categoriesId));
        repository.delete(categories);
        return MapperCategories.toResponse(categories);
    }

    public ResponseCategoriesDto update(RequestCategoriesDto request, Long categoriesId) {
        Categories categories = repository.findById(categoriesId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + categoriesId));
        return MapperCategories.toResponse(repository.save(MapperCategories.toCategories(request)));
    }

    public ResponseCategoriesDto get(Long categoriesId) {
        return MapperCategories.toResponse(repository.findById(categoriesId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + categoriesId)));
    }

    public List<ResponseCategoriesDto> getAll() {
        return repository.findAll().stream()
                .map(MapperCategories::toResponse)
                .collect(Collectors.toList());
    }
}
