package ru.practicum.exploreWithMe.stats.request.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.exploreWithMe.stats.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.exploreWithMe.stats.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.stats.request.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/users/{userId}")
public class PrivateRequestController {
    private final RequestService service;

    @PostMapping("/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(
            @PathVariable Long userId,
            @RequestParam Long eventId
    ) {
        log.info("Пришел POST запрос /users/{}/requests?eventId={}", userId, eventId);
        final ParticipationRequestDto response = service.create(userId, eventId);
        log.info("Отправлен ответ для POST запроса /users/{}/requests?eventId={} с телом: {}", userId, eventId, response);
        return response;
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ParticipationRequestDto canceled(
            @PathVariable Long userId,
            @PathVariable Long requestId
    ) {
        log.info("Пришел PATCH запрос /users/{}/requests/{}/cancel", userId, requestId);
        final ParticipationRequestDto response = service.canceled(userId, requestId);
        log.info("Отправлен ответ для PATCH запроса /users/{}/requests/{}/cancel с телом: {}", userId, requestId, response);
        return response;
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult update(
            @RequestBody EventRequestStatusUpdateRequest request,
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("Пришел PATCH запрос /users/{}/events/{}/requests с телом: {}", userId, eventId, request);
        final EventRequestStatusUpdateResult response = service.update(userId, eventId, request);
        log.info("Отправлен ответ для PATCH запроса /users/{}/events/{}/requests с телом: {}", userId, eventId, response);
        return response;
    }

    @GetMapping("/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllUser(
            @PathVariable Long userId,
            @PathVariable Long eventId
    ) {
        log.info("Пришел GET запрос /users/{}/events/{}/requests", userId, eventId);
        final List<ParticipationRequestDto> response = service.getAllUser(userId, eventId);
        log.info("Отправлен ответ для GET запроса /users/{}/events/{}/requests с телом: {}", userId, eventId, response);
        return response;
    }

    @GetMapping("/requests")
    public List<ParticipationRequestDto> getAll(
            @PathVariable Long userId
    ) {
        log.info("Пришел GET запрос /users/{}/requests", userId);
        final List<ParticipationRequestDto> response = service.getAll(userId);
        log.info("Отправлен ответ для GET запроса /users/{}/requests с телом: {}", userId, response);
        return response;
    }
}
