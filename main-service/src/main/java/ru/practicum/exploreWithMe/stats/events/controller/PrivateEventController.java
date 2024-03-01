package ru.practicum.exploreWithMe.stats.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.events.dto.EventFullDto;
import ru.practicum.exploreWithMe.stats.events.dto.EventShortDto;
import ru.practicum.exploreWithMe.stats.events.dto.NewEventDto;
import ru.practicum.exploreWithMe.stats.events.dto.UpdateEventUserRequest;
import ru.practicum.exploreWithMe.stats.events.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final EventService service;

    @PostMapping
    public EventFullDto create(
            @RequestBody @Valid NewEventDto event,
            @PathVariable Long userId
    ) {
        log.info("Пришел POST запрос users/{}/events с телом: {}", userId, event);
        final EventFullDto response = service.create(event, userId);
        log.info("Отправлен ответ для POST запроса users/{}/events с телом: {}", userId, response);
        return response;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(
            @RequestBody UpdateEventUserRequest event,
            @PathVariable Long userId,
            @PathVariable Long eventId,
            HttpServletRequest request
    ) {
        log.info("Пришел PATCH запрос users/{}/events/{} с телом: {}", userId, eventId, event);
        final EventFullDto response = service.update(event, userId, eventId, request);
        log.info("Отправлен ответ для PATCH запроса users/{}/events/{} с телом: {}", userId, eventId, response);
        return response;
    }

    @GetMapping
    public List<EventShortDto> getAll(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Пришел GET запрос users/{}/events?from={}&size={}", userId, from, size);
        final List<EventShortDto> response = service.getAllUserId(userId, from, size);
        log.info("Отправлен ответ для GET запроса users/{}/events?from={}&size={} с телом: {}", userId, from, size, response);
        return response;
    }

    @GetMapping("/{eventId}")
    public EventFullDto get(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("Пришел GET запрос users/{}/events/{}", userId, eventId);
        final EventFullDto response = service.getUserIdAndEventId(userId, eventId);
        log.info("Отправлен ответ для GET запроса users/{}/events/{} с телом: {}", userId, eventId, response);
        return response;
    }


}
