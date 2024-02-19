package ru.practicum.controller;


import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseGetStatsDto;
import ru.practicum.dto.ResponseStatsDto;
import ru.practicum.service.StatsService;



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
        log.info("Пришел GET запрос /stats с параметрами, start:{}, end:{}, uris:{}, unique:{}", start, end, uris, unique);
        List<ResponseGetStatsDto> response = service.get(start, end, uris, unique);
        log.info("Отправлен ответ для GET запроса /stats с параметрами, start:{}, end:{}, uris:{}, unique:{} ответ:{}", start, end, uris, unique, response);
        return  response;
    }
}
