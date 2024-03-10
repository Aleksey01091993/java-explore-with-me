package ru.practicum.exploreWithMe.stats.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.client.StatsClient;
import ru.practicum.exploreWithMe.stats.events.dto.EventFullDto;
import ru.practicum.exploreWithMe.stats.events.mapper.EventsMapper;
import ru.practicum.exploreWithMe.stats.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/events")
public class PublicEventController {
    private final EventService service;
    private final StatsClient client;

    @GetMapping
    public ResponseEntity<Object> getAll(
            @RequestParam @Nullable String text,
            @RequestParam @Nullable List<Long> categories,
            @RequestParam @Nullable Boolean paid,
            @RequestParam @Nullable String rangeStart,
            @RequestParam @Nullable String rangeEnd,
            @RequestParam @Nullable Boolean onlyAvailable,
            @RequestParam @Nullable String sort,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request
    ) {
        log.info("Пришел GET запрос /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&from={}&size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        client.hit(request);
        final ResponseEntity<Object> response = service
                .getAll(EventsMapper.toEventFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size));
        log.info("Отправлен ответ для GET запроса /events?text={}&categories={}&paid={}&rangeStart={}&rangeEnd={}&onlyAvailable={}&sort={}&from={}&size={} с телом: {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, response);
        return response;
    }

    @GetMapping("/{eventId}")
    public EventFullDto get(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Пришел GET запрос /events/{}", eventId);
        client.hit(request);
        final EventFullDto response = service.get(eventId, request);
        log.info("Отправлен ответ для GET запроса /events/{} с телом: {}", eventId, response);
        return response;
    }
}
