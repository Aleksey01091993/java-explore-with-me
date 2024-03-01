package ru.practicum.exploreWithMe.stats.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.compilations.dto.CompilationDto;
import ru.practicum.exploreWithMe.stats.compilations.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.stats.compilations.dto.UpdateCompilationRequest;
import ru.practicum.exploreWithMe.stats.compilations.service.CompilationService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService service;

    @PostMapping
    public CompilationDto create(NewCompilationDto compilation) {
        log.info("Пришел POST запрос /admin/compilations с телом: {}", compilation);
        final CompilationDto response = service.create(compilation);
        log.info("Отправлен ответ для POST запроса /admin/compilations с телом: {}", response);
        return response;
    }

    @PatchMapping("/{compilationId}")
    public CompilationDto update(UpdateCompilationRequest compilationNew, @PathVariable Long compilationId) {
        log.info("Пришел PATCH запрос /admin/compilations/{} с телом: {}", compilationId, compilationNew);
        final CompilationDto response = service.update(compilationNew, compilationId);
        log.info("Отправлен ответ для POST запроса /admin/compilations/{} с телом: {}", compilationId, response);
        return response;
    }

    @DeleteMapping("/{compilationId}")
    public CompilationDto delete(@PathVariable Long compilationId) {
        log.info("Пришел DELETE запрос /admin/compilations/{}", compilationId);
        final CompilationDto response = service.delete(compilationId);
        log.info("Отправлен ответ для DELETE запроса /admin/compilations/{} с телом: {}", compilationId, response);
        return response;
    }


}
