package ru.practicum.exploreWithMe.stats.coments.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
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

import java.time.LocalDateTime;
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


    public CommentResponse create(Long userId, Long eventId, CommentRequest commentRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        Event event = eventsRepository.findFirstByIdAndState(eventId, Status.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("event not found by id: " + userId
                        + "or comments can only be left by the completed event"));
        Comments comments = repository.save(CommentMapper.toComment(commentRequest, user, event));
        return CommentMapper.toResponse(comments);
    }


    public CommentResponse update(Long commentsId, Long authorId, CommentRequest request) {
        Comments comments = repository.findFirstByIdAndAuthor_Id(commentsId, authorId)
                .orElseThrow(() -> new NotFoundException("user or comments not found by id"));
        return CommentMapper.toResponse(CommentMapper.toUpdate(request, comments));
    }


    public void delete(Long commentId, Long authorId) {
        Comments comments = repository.findFirstByIdAndAuthor_Id(commentId, authorId)
                .orElseThrow(() -> new NotFoundException("user or comments not found by id"));
        repository.delete(comments);
    }


    public void deleteAdmin(List<Long> commentsIds) {
        if (commentsIds != null || !commentsIds.isEmpty()) {
            repository.deleteAllById(commentsIds);
        }
    }


    public CommentResponse get(Long commentId) {
        return CommentMapper.toResponse(repository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment not found by id: " + commentId)));
    }


    public List<CommentResponse> getAll(List<Long> usersIds,
                                        List<Long> eventsIds,
                                        List<Long> commentsIds,
                                        LocalDateTime start,
                                        LocalDateTime end) {
        {
            BooleanExpression expression = this.expression(start, end, usersIds, eventsIds, commentsIds);
            if (expression == null) {
                return repository.findAll().stream().map(CommentMapper::toResponse).collect(Collectors.toList());
            } else {
                List<Comments> comments = new ArrayList<>();
                repository.findAll(expression).forEach(comments::add);
                return comments.stream().map(CommentMapper::toResponse).collect(Collectors.toList());
            }
        }


    }

    private BooleanExpression expression(LocalDateTime startTime,
                                         LocalDateTime endTime,
                                         List<Long> usersIds,
                                         List<Long> eventsIds,
                                         List<Long> commentsIds) {

        List<BooleanExpression> expressionsOr = new ArrayList<>();
        List<BooleanExpression> expressionsAnd = new ArrayList<>();
            Optional.ofNullable(startTime).ifPresent(o1 -> expressionsAnd.add(QComments.comments.created.after(o1)));
            Optional.ofNullable(endTime).ifPresent(o1 -> expressionsAnd.add(QComments.comments.created.before(o1)));
            Optional.ofNullable(usersIds).ifPresent(o1 -> expressionsOr.add(QComments.comments.author.id.in(o1)));
            Optional.ofNullable(eventsIds).ifPresent(o1 -> expressionsOr.add(QComments.comments.event.id.in(o1)));
            Optional.ofNullable(commentsIds).ifPresent(o1 -> expressionsOr.add(QComments.comments.id.in(o1)));

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : expressionsOr) {
            result = result.or(predicate);
        }
        for (BooleanExpression predicate : expressionsAnd) {
            result = result.and(predicate);
        }
        return result;
    }


}
