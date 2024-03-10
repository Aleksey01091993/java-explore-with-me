package ru.practicum.exploreWithMe.stats.coments.mapper;

import ru.practicum.exploreWithMe.stats.coments.dto.CommentRequest;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentResponse;
import ru.practicum.exploreWithMe.stats.coments.model.Comments;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.users.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comments toComment(CommentRequest commentRequest, User author, Event event) {
        return Comments.builder()
                .text(commentRequest.getText())
                .event(event)
                .author(author)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentResponse toResponse(Comments comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comments toUpdate(CommentRequest commentRequest, Comments comments) {
        return Comments.builder()
                .id(comments.getId())
                .text(commentRequest.getText())
                .event(comments.getEvent())
                .author(comments.getAuthor())
                .created(LocalDateTime.now())
                .build();
    }


}
