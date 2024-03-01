package ru.practicum.exploreWithMe.stats.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.categories.dto.CategoryDto;
import ru.practicum.exploreWithMe.stats.categories.dto.NewCategoryDto;
import ru.practicum.exploreWithMe.stats.categories.service.CategoriesService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/categories")
public class AdminCategoriesController {
    private final CategoriesService service;

    @PostMapping
    public CategoryDto add(
            @RequestBody NewCategoryDto category
    ) {
        log.info("Пришел POST запрос /admin/categories с телом: {}", category);
        final CategoryDto categoryResponse = service.add(category);
        log.info("Отправлен ответ для POST запроса /admin/categories с телом: {}", categoryResponse);
        return categoryResponse;
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(
            @PathVariable Long catId,
            @RequestBody NewCategoryDto category
    ) {
        log.info("Пришел PATH запрос /admin/categories/{} с телом: {}", catId, category);
        final CategoryDto categoryResponse = service.update(category, catId);
        log.info("Отправлен ответ для PATH запроса /admin/categories/{} с телом: {}", catId, categoryResponse);
        return categoryResponse;
    }

    @DeleteMapping("/{catId}")
    public CategoryDto delete(
            @PathVariable Long catId
    ) {
        log.info("Пришел DELETE запрос /admin/categories/{}", catId);
        final CategoryDto categoryResponse = service.delete(catId);
        log.info("Отправлен ответ для DELETE запроса /admin/categories/{} с телом: {}", catId, categoryResponse);
        return categoryResponse;
    }
}
