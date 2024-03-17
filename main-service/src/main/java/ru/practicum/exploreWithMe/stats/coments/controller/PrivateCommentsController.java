package ru.practicum.exploreWithMe.stats.coments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentRequest;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentResponse;
import ru.practicum.exploreWithMe.stats.coments.service.CommentsService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users/{userId}")
public class PrivateCommentsController {
    private final CommentsService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/events/{eventId}/comments")
    public CommentResponse create(@PathVariable Long userId,
                                  @PathVariable Long eventId,
                                  @RequestBody @Valid CommentRequest request) {
        log.info("Пришел POST запрос /users/{}/events/{}/comments с телом: {}", userId, eventId, request);
        CommentResponse response = service.create(userId, eventId, request);
        log.info("Отправлен ответ для POST запроса /users/{}/events/{}/comments с телом: {}", userId, eventId, request);
        return response;
    }

    @PatchMapping("/comments/{commentId}")
    public CommentResponse update(@PathVariable Long userId,
                                  @PathVariable Long commentId,
                                  @RequestBody @Valid CommentRequest request) {
        log.info("Пришел PATCH запрос /users/{}/comments/{} с телом: {}", userId, commentId, request);
        CommentResponse response = service.update(commentId, userId, request);
        log.info("Отправлен ответ для PATCH запроса /users/{}/comments/{} с телом: {}", userId, commentId, request);
        return response;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/comments/{commentId}")
    public void delete(@PathVariable Long userId,
                       @PathVariable Long commentId) {
        log.info("Пришел DELETE запрос /users/{}/comments/{}", userId, commentId);
        service.delete(commentId, userId);
        log.info("Запрос выполнен");
    }

}
