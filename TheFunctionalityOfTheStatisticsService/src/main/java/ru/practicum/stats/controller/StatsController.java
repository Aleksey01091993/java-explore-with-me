package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.RequestStatsDto;
import ru.practicum.stats.dto.ResponseGetStatsDto;
import ru.practicum.stats.dto.ResponseStatsDto;
import ru.practicum.stats.service.StatsService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class StatsController {

    private final StatsService service;

    @PostMapping("/hit")
    public ResponseStatsDto create
            (
                    @RequestBody @NotNull RequestStatsDto requestStatsDto
            ) {
        log.info("Пришел POST запрос /hit с телом: {}", requestStatsDto);
        ResponseStatsDto response = service.add(requestStatsDto);
        log.info("Отправлен ответ для POST запроса /hit с телом: {}", response);
        return response;
    }

    @GetMapping("/stats")
    public List<ResponseGetStatsDto> get
            (
                    @RequestParam String start,
                    @RequestParam String end,
                    @RequestParam @Nullable List<String> uris,
                    @RequestParam @Nullable Boolean unique
            ) {
        log.info("Пришел GET запрос /stats с параметрами, start:{}, end:{}, uris:{}, unique:{}", start, end, unique, unique);
        List<ResponseGetStatsDto> response = service.get(start, end, uris, unique);
        log.info("Отправлен ответ для GET запроса /stats с параметрами, start:{}, end:{}, uris:{}, unique:{} ответ:{}", start, end, unique, unique, response);
        return  response;
    }
}
