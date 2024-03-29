package ru.practicum.exploreWithMe.stats.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.categories.dto.CategoryDto;
import ru.practicum.exploreWithMe.stats.categories.dto.NewCategoryDto;
import ru.practicum.exploreWithMe.stats.categories.mapper.MapperCategories;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.categories.repository.CategoriesRepository;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.ConflictError;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {
    private final CategoriesRepository repository;
    private final EventsRepository eventsRepository;

    public CategoryDto add(NewCategoryDto request) {
        if (request.getName() != null) {
            if (repository.findFirstByName(request.getName()).isPresent()) {
                throw new ConflictError("Adding a new category with a busy name");
            }
        }
        return MapperCategories.toCategoryDto(repository.save(MapperCategories.toCategories(request)));
    }

    public CategoryDto delete(Long catId) {
        Categories categories = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + catId));
        if (!eventsRepository.findByCategory(categories).isEmpty()) {
            throw new ConflictError("Deleting a category with linked events");
        }
        repository.delete(categories);
        return MapperCategories.toCategoryDto(categories);
    }

    public CategoryDto update(NewCategoryDto request, Long catId) {
        Categories categories = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + catId));
        if (request.getName() != null) {
            if (repository.findFirstByName(request.getName()).isPresent()
                    && !(categories.getName().equals(request.getName()))) {
                throw new ConflictError("Attempt to change the category name to an existing one");
            }
        }
        categories.setName(request.getName());
        return MapperCategories.toCategoryDto(repository.save(categories));
    }

    public CategoryDto get(Long catId) {
        return MapperCategories.toCategoryDto(repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("categories not found by id: " + catId)));
    }

    public List<CategoryDto> getAll(Integer from, Integer size) {
        return repository.findAll(PageRequest.of(from/ size, size)).stream()
                .map(MapperCategories::toCategoryDto)
                .collect(Collectors.toList());
    }
}
