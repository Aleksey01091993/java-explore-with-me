package ru.practicum.exploreWithMe.stats.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.events.dto.EventFullDto;
import ru.practicum.exploreWithMe.stats.events.dto.EventShortDto;
import ru.practicum.exploreWithMe.stats.events.dto.UpdateEventAdminRequest;
import ru.practicum.exploreWithMe.stats.events.mapper.EventsMapper;
import ru.practicum.exploreWithMe.stats.events.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping( "/admin/events")
public class AdminEventController {
    private final EventService service;

    @PatchMapping("/{eventId}")
    public EventFullDto update(
            @RequestBody UpdateEventAdminRequest event,
            @PathVariable Long eventId
    ) {
        log.info("Пришел PATCH запрос /admin/events/{} с телом: {}", eventId, event);
        final EventFullDto response = service.updateAdmin(event, eventId);
        log.info("Отправлен ответ для PATCH запроса /admin/events/{} с телом: {}", eventId, response);
        return response;
    }

    @GetMapping
    public List<EventFullDto> getAll(
            @RequestParam @Nullable List<Long> users,
            @RequestParam @Nullable List<String> states,
            @RequestParam @Nullable List<Long> categories,
            @RequestParam @Nullable String rangeStart,
            @RequestParam @Nullable String rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        log.info("Пришел GET запрос /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        final List<EventFullDto> response = service
                .getAllAdmin(EventsMapper.toEventFilterAdmin(states, users, categories, rangeStart, rangeEnd, from, size));
        log.info("Отправлен ответ для GET запроса /admin/events?users={}&states={}&categories={}&rangeStart={}&rangeEnd={}&from={}&size={} с телом: {}",
                users, states, categories, rangeStart, rangeEnd, from, size, response);
        return response;
    }
}
