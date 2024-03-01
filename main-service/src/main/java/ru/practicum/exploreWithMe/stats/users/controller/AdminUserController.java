package ru.practicum.exploreWithMe.stats.users.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.users.dto.NewUserRequest;
import ru.practicum.exploreWithMe.stats.users.dto.UserDto;
import ru.practicum.exploreWithMe.stats.users.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/users")
public class AdminUserController {
    private final UserService service;

    @PostMapping
    public UserDto add(
            @RequestBody NewUserRequest user
    ) {
        log.info("Пришел POST запрос admin/users с телом: {}", user);
        final UserDto userResponse = service.add(user);
        log.info("Отправлен ответ для POST запроса /admin/users с телом: {}", userResponse);
        return userResponse;
    }

    @GetMapping
    public List<UserDto> get(
            @RequestParam @Nullable List<Long> ids,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        log.info("Пришел GET запрос admin/users с параметрами: ids={}; from={}; size={}", ids, from, size);
        final List<UserDto> userResponse = service.getAll(ids, from, size);
        log.info("Отправлен ответ для GET запроса admin/users с параметрами: ids={}; from={}; size={}; Ответ:={}",
                ids, from, size, userResponse);
        return userResponse;
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(
            @PathVariable Long userId
    ) {
        log.info("Пришел DELETE запрос admin/users/{}", userId);
        final UserDto userResponse = service.delete(userId);
        log.info("Отправлен ответ для DELETE запроса с телом: {}", userResponse);
        return userResponse;
    }
}
