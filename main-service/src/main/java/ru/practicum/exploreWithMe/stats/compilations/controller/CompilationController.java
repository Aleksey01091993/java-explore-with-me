package ru.practicum.exploreWithMe.stats.compilations.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.compilations.dto.CompilationDto;
import ru.practicum.exploreWithMe.stats.compilations.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService service;

    @GetMapping
    public List<CompilationDto> getAll
            (@RequestParam @Nullable Boolean pinned,
             @RequestParam(defaultValue = "0") Integer from,
             @RequestParam(defaultValue = "10") Integer size
            ) {
        log.info("Пришел GET запрос /compilations?pinned={}&from={}&size={}", pinned, from, size);
        final List<CompilationDto> response = service.getAll(pinned, from, size);
        log.info("Отправлен ответ для GET запроса /compilations?pinned={}&from={}&size={} с телом: {}", pinned, from, size, response);
        return response;
    }

    @GetMapping("/{compId}")
    public CompilationDto get(@PathVariable Long compId) {
        log.info("Пришел GET запрос /compilations/{}", compId);
        final CompilationDto response = service.get(compId);
        log.info("Отправлен ответ для GET запроса /compilations/{} с телом: {}", compId, response);
        return response;
    }


}
