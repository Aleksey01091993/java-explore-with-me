package ru.practicum.exploreWithMe.stats.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.categories.dto.CategoryDto;
import ru.practicum.exploreWithMe.stats.categories.service.CategoriesService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/categories")
public class PublicCategoriesController {
    private final CategoriesService service;

    @GetMapping
    public List<CategoryDto> getAll(
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        log.info("Пришел GET запрос /categories с параметрами: from={}; size={}", from, size);
        final List<CategoryDto> categoryResponse = service.getAll(from, size);
        log.info("Отправлен ответ для GET запроса /categories с телом: {}", categoryResponse);
        return categoryResponse;
    }

    @GetMapping("/{catId}")
    public CategoryDto get(
            @PathVariable Long catId
    ) {
        log.info("Пришел GET запрос /categories/{}", catId);
        final CategoryDto categoryResponse = service.get(catId);
        log.info("Отправлен ответ для GET запроса /categories/{} с телом: {}", catId, categoryResponse);
        return categoryResponse;
    }
}
