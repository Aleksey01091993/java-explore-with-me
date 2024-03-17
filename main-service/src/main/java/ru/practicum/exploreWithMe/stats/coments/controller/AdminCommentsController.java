package ru.practicum.exploreWithMe.stats.coments.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentResponse;
import ru.practicum.exploreWithMe.stats.coments.service.CommentsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping( "/admin/comments")
public class AdminCommentsController {
    private final CommentsService service;


    @DeleteMapping
    public ResponseEntity<Object> deleteAdmin(@RequestParam @Nullable List<Long> ids) {
        log.info("Пришел DELETE запрос /admin/comments?ids={}", ids);
        ResponseEntity<Object> response = service.deleteAdmin(ids);
        log.info("Запрос выполнен, ответ : {}", response);
        return response;
    }

    @GetMapping("/{commentId}")
    public CommentResponse get(@PathVariable Long commentId) {
        log.info("Пришел GET запрос /admin/comments/{}", commentId);
        CommentResponse response = service.get(commentId);
        log.info("Отправлен ответ для GET запроса /admin/comments/{} с телом: {}", commentId, response);
        return response;
    }


    @GetMapping
    public List<CommentResponse> getAll(@RequestParam @Nullable List<Long> userIds,
                                        @RequestParam @Nullable List<Long> eventIds,
                                        @RequestParam @Nullable List<Long> commentIds,
                                        @RequestParam @Nullable String text,
                                        @RequestParam @Nullable String start,
                                        @RequestParam @Nullable String end) {

        log.info("Пришел GET запрос /admin/comments?userIds={}&eventIds={}&commentIds={}&start={}&end={}",
                userIds, eventIds, commentIds, start, end);
        List<CommentResponse> response = service.getAll(userIds, eventIds, commentIds, start, end, text);
        log.info("Отправлен ответ для GET запроса /admin/comments?userIds={}&eventIds={}&commentIds={}&start={}&end={} с телом: {}",
                userIds, eventIds, commentIds, start, end, response);
        return response;
    }

}

