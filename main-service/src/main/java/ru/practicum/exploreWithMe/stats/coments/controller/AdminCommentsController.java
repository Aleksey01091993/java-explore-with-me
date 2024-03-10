package ru.practicum.exploreWithMe.stats.coments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentResponse;
import ru.practicum.exploreWithMe.stats.coments.service.CommentsService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping( "/admin/comments")
public class AdminCommentsController {
    private final CommentsService service;

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping
    public void deleteAdmin(@RequestParam @Nullable List<Long> ids) {
        log.info("Пришел DELETE запрос /admin/comments?ids={}", ids);
        service.deleteAdmin(ids);
        log.info("Запрос выполнен");
    }

    @GetMapping("/{commentId}")
    public CommentResponse get(@PathVariable Long commentId) {
        log.info("Пришел GET запрос /admin/comments/{}", commentId);
        CommentResponse response = service.get(commentId);
        log.info("Отправлен ответ для GET запроса /admin/comments/{} с телом: {}", commentId, response);
        return response;
    }

    @GetMapping
    public List<CommentResponse> getAll(@RequestParam @Nullable List<Long> usersIds,
                                        @RequestParam @Nullable List<Long> eventIds,
                                        @RequestParam @Nullable List<Long> commentsIds,
                                        @RequestParam @Nullable LocalDateTime start,
                                        @RequestParam @Nullable LocalDateTime end) {

        log.info("Пришел GET запрос /admin/comments?usersIds={}&eventIds={}&commentsIds={}&start={}&end={}",
                usersIds, eventIds, commentsIds, start, end);
        List<CommentResponse> response = service.getAll(usersIds, eventIds, commentsIds, start, end);
        log.info("Отправлен ответ для GET запроса /admin/comments?usersIds={}&eventIds={}&commentsIds={}&start={}&end={} с телом: {}",
                usersIds, eventIds, commentsIds, start, end, response);
        return response;
    }

}

