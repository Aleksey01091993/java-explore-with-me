package ru.practicum.exploreWithMe.stats.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.categories.repository.CategoriesRepository;
import ru.practicum.exploreWithMe.stats.client.StatsClient;
import ru.practicum.exploreWithMe.stats.dto.ResponseGetStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseStatsDto;
import ru.practicum.exploreWithMe.stats.events.dto.*;
import ru.practicum.exploreWithMe.stats.events.mapper.EventsMapper;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.BadRequestException;
import ru.practicum.exploreWithMe.stats.exception.ConflictError;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;
import ru.practicum.exploreWithMe.stats.querydsl.EventFilterModel;
import ru.practicum.exploreWithMe.stats.querydsl.EventPredicatesBuilder;
import ru.practicum.exploreWithMe.stats.statuses.StateAction;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventsRepository repository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final StatsClient client;


    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto create(NewEventDto event, Long id) {
        LocalDateTime find = LocalDateTime.now().plusHours(2L);
        LocalDateTime eventDate = LocalDateTime.parse(event.getEventDate(), DTF);
        if (eventDate.isBefore(find)) {
            throw new BadRequestException("the date and time at which the event is scheduled cannot be earlier than two hours from the current moment");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + id));
        Categories categories = categoriesRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + event.getCategory()));
        Event eventSave = EventsMapper.toCreate(event);
        eventSave.setInitiator(user);
        eventSave.setCategory(categories);
        Event response = repository.save(eventSave);
        return EventsMapper.toEventCreate(response);
    }

    public EventFullDto update(UpdateEventUserRequest event, Long userId, Long eventId, HttpServletRequest request) {
        Event eventToUpdate = repository.findFirstByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("event or user not found by id: " + eventId + "," + userId));
        if (!(eventToUpdate.getState() == Status.REJECTED || eventToUpdate.getState() == Status.PENDING)) {
            throw new ConflictError("you can only change canceled events or events in the waiting state of moderation");
        }
        LocalDateTime find = LocalDateTime.now().plusHours(2L);
        if (event.getEventDate() == null) {
            if (eventToUpdate.getEventDate().isBefore(find)) {
                throw new BadRequestException("the date and time at which the event is scheduled cannot be earlier than two hours from the current moment");
            }
        } else {
            LocalDateTime newEventDate = LocalDateTime.parse(event.getEventDate(), DTF);
            if (newEventDate.isBefore(find)) {
                throw new BadRequestException("the date and time at which the event is scheduled cannot be earlier than two hours from the current moment");
            }
        }
        Categories category;
        if (event.getCategory() != null) {
            category = categoriesRepository.findById(event.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found by id: " + event.getCategory()));
        } else {
            category = null;
        }
        return EventsMapper.toEventFullDto(repository.save(EventsMapper.toUpdate(eventToUpdate, event, category)));
    }

    public List<EventShortDto> getAllUserId(Long userId, Integer from, Integer size) {
        return repository.findAllByInitiator_Id(userId, PageRequest.of(from/size, size)).stream()
                .map(EventsMapper::toGetAll)
                .collect(Collectors.toList());
    }

    public EventFullDto getUserIdAndEventId(Long userId, Long eventId) {
        return EventsMapper.toEventFullDto(
                repository.findFirstByIdAndInitiator_Id(eventId, userId)
                        .orElseThrow(() -> new NotFoundException("event or user not found by id: " + eventId + "," + userId))
        );
    }

    public ResponseEntity<Object> getAll(EventFilterModel filter) {
        EventPredicatesBuilder builder = new EventPredicatesBuilder();
        if (filter.getRangeEnd() == null && filter.getRangeStart() == null) {
            builder.with("eventStart", LocalDateTime.now());
        } else {
            Optional.ofNullable(filter.getRangeStart())
                    .ifPresent(o1 -> builder.with("eventStart", filter.getRangeStart()));
            Optional.ofNullable(filter.getRangeEnd())
                    .ifPresent(o1 -> builder.with("eventEnd", filter.getRangeEnd()));
        }
        Optional.ofNullable(filter.getStatuses())
                .ifPresent(o1 -> builder.with("states", List.of(Status.PUBLISHED)));
        Optional.ofNullable(filter.getText())
                .ifPresent(o1 -> builder.with("text", filter.getText()));
        Optional.ofNullable(filter.getCategories())
                .ifPresent(o1 -> builder.with("category", filter.getCategories()));
        Optional.ofNullable(filter.getPaid())
                .ifPresent(o1 -> builder.with("paid", filter.getPaid()));
        Optional.ofNullable(filter.getOnlyAvailable())
                .ifPresent(o1 -> builder.with("available", filter.getOnlyAvailable()));
        BooleanExpression expression = builder.build();
        List<EventShortDto> response = repository.findAll(expression, filter.getPageable())
                .stream()
                .map(EventsMapper::toGetAll)
                .collect(Collectors.toList());
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    public EventFullDto get(Long eventId, HttpServletRequest request) {
        Event event = repository.findFirstByIdAndState(eventId, Status.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId));
        List<ResponseGetStatsDto> stats = client.getStats(request, "/" + eventId);
        Optional.ofNullable(stats).ifPresent(o1 -> event.setViews(o1.get(0).getHits()));
        return EventsMapper.toEventFullDto(event);
    }

    public List<EventFullDto> getAllAdmin(EventFilterModel filter) {
        EventPredicatesBuilder builder = new EventPredicatesBuilder();
        Optional.ofNullable(filter.getUsersId())
                .ifPresent(o1 -> builder.with("users", filter.getUsersId()));
        Optional.ofNullable(filter.getCategories())
                .ifPresent(o1 -> builder.with("category", filter.getCategories()));
        Optional.ofNullable(filter.getStatuses())
                .ifPresent(o1 -> builder.with("states", filter.getStatuses()));
        Optional.ofNullable(filter.getRangeStart())
                .ifPresent(o1 -> builder.with("eventStart", filter.getRangeStart()));
        Optional.ofNullable(filter.getRangeEnd())
                .ifPresent(o1 -> builder.with("eventEnd", filter.getRangeEnd()));
        BooleanExpression expression = builder.build();
        if (expression == null) {
            return repository.findAll(filter.getPageable())
                    .stream()
                    .map(EventsMapper::toEventFullDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAll(expression, filter.getPageable())
                    .stream()
                    .map(EventsMapper::toEventFullDto)
                    .collect(Collectors.toList());
        }

    }

    public EventFullDto updateAdmin(UpdateEventAdminRequest event, Long eventId) {
        Categories category;
        if (event.getCategory() != null) {
            category = categoriesRepository.findById(event.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found by id: " + event.getCategory()));
        } else {
            category = null;
        }
        Event eventToUpdate = repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId));
        LocalDateTime find = LocalDateTime.now().plusHours(1L);
        if (event.getEventDate() == null) {
            if (eventToUpdate.getEventDate().isBefore(find)) {
                throw new BadRequestException("the date and time at which the event is scheduled cannot be earlier than two hours from the current moment");
            }
        } else {
            LocalDateTime newEventDate = LocalDateTime.parse(event.getEventDate(), DTF);
            if (newEventDate.isBefore(find)) {
                throw new BadRequestException("the date and time at which the event is scheduled cannot be earlier than two hours from the current moment");

            }
        }
        if ((event.getStateAction() != null) &&
                ((event.getStateAction() == StateAction.PUBLISH_EVENT || event.getStateAction() == StateAction.REJECT_EVENT) &&
                        eventToUpdate.getState() != Status.PENDING)) {
            throw new ConflictError("An event can be published only if it is in the waiting state for publication or" +
                    " An event can be rejected only if it has not been published yet");
        }
        Event eventSave = EventsMapper.toUpdate(eventToUpdate, event, category);
        return EventsMapper.toEventFullDto(repository.save(eventSave));
    }



}
