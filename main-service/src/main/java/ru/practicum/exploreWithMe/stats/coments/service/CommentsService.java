package ru.practicum.exploreWithMe.stats.coments.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.Iterators;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentRequest;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentResponse;
import ru.practicum.exploreWithMe.stats.coments.mapper.CommentMapper;
import ru.practicum.exploreWithMe.stats.coments.model.Comments;
import ru.practicum.exploreWithMe.stats.coments.model.QComments;
import ru.practicum.exploreWithMe.stats.coments.repository.CommentsRepository;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;

import javax.swing.text.html.HTMLDocument;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository repository;
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public CommentResponse create(Long userId, Long eventId, CommentRequest commentRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        Event event = eventsRepository.findFirstByIdAndState(eventId, Status.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId
                        + " or comments can only be left by the completed event"));
        Comments comments = repository.save(CommentMapper.toComment(commentRequest, user, event));
        return CommentMapper.toResponse(comments);
    }


    public CommentResponse update(Long commentsId, Long authorId, CommentRequest request) {
        Comments comments = repository.findFirstByIdAndAuthor_Id(commentsId, authorId)
                .orElseThrow(() -> new NotFoundException("user or comments not found by id"));
        Comments newComments = repository.save(CommentMapper.toUpdate(request, comments));
        CommentResponse response = CommentMapper.toResponse(newComments);
        return response;
    }


    public void delete(Long commentId, Long authorId) {
        Comments comments = repository.findFirstByIdAndAuthor_Id(commentId, authorId)
                .orElseThrow(() -> new NotFoundException("user or comments not found by id"));
        repository.delete(comments);
    }


    public ResponseEntity<Object> deleteAdmin(List<Long> commentsIds) {
        if (commentsIds != null) {
            List<Comments> comments = repository.findAllByIdIn(commentsIds);
            if (!comments.isEmpty()) {
                if (comments.size() < commentsIds.size()) {
                    repository.deleteAllById(commentsIds);
                    return new ResponseEntity<>(HttpEntity.EMPTY, HttpStatus.NON_AUTHORITATIVE_INFORMATION);
                } else {
                    repository.deleteAllById(commentsIds);
                    return new ResponseEntity<>(HttpEntity.EMPTY, HttpStatus.NO_CONTENT);
                }
            } else {
                throw new NotFoundException("commentsIds not found by id");
            }
        } else {
            throw new NotFoundException("commentsIds is null");
        }
    }


    public CommentResponse get(Long commentId) {
        return CommentMapper.toResponse(repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment not found by id: " + commentId)));
    }


    public List<CommentResponse> getAll(List<Long> userIds,
                                        List<Long> eventIds,
                                        List<Long> commentIds,
                                        String start,
                                        String end,
                                        String text) {
        {
            BooleanExpression expression = this.expression(start, end, userIds, eventIds, commentIds, text);
            if (expression == null) {
                return repository.findAll().stream().map(CommentMapper::toResponse).collect(Collectors.toList());
            } else {
                List<Comments> comments = new ArrayList<>();
                repository.findAll(expression).forEach(comments::add);
                return comments.stream().map(CommentMapper::toResponse).collect(Collectors.toList());
            }
        }


    }

    private BooleanExpression expression(String startTime,
                                         String endTime,
                                         List<Long> userIds,
                                         List<Long> eventIds,
                                         List<Long> commentIds,
                                         String text) {
        LocalDateTime start = startTime == null || startTime.isEmpty() ? null : LocalDateTime.parse(startTime, DTF);
        LocalDateTime end = endTime == null || endTime.isEmpty() ? null : LocalDateTime.parse(endTime, DTF);
        List<BooleanExpression> expressions = new ArrayList<>();
            Optional.ofNullable(start).ifPresent(o1 -> expressions.add(QComments.comments.created.after(o1)));
            Optional.ofNullable(end).ifPresent(o1 -> expressions.add(QComments.comments.created.before(o1)));
            Optional.ofNullable(userIds).ifPresent(o1 -> expressions.add(QComments.comments.author.id.in(o1)));
            Optional.ofNullable(eventIds).ifPresent(o1 -> expressions.add(QComments.comments.event.id.in(o1)));
            Optional.ofNullable(commentIds).ifPresent(o1 -> expressions.add(QComments.comments.id.in(o1)));
            Optional.ofNullable(text).ifPresent(o1 -> expressions.add(QComments.comments.text.containsIgnoreCase(o1)));
        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : expressions) {
            result = result.and(predicate);
        }
        return result;
    }


}
