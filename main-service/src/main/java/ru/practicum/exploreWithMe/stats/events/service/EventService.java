package ru.practicum.exploreWithMe.stats.events.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.categories.repository.CategoriesRepository;
import ru.practicum.exploreWithMe.stats.events.dto.EventFullDto;
import ru.practicum.exploreWithMe.stats.events.dto.EventShortDto;
import ru.practicum.exploreWithMe.stats.events.dto.NewEventDto;
import ru.practicum.exploreWithMe.stats.events.mapper.EventsMapper;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;
import ru.practicum.exploreWithMe.stats.querydsl.EventFilterModel;
import ru.practicum.exploreWithMe.stats.querydsl.EventPredicatesBuilder;
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;

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

    public EventFullDto create(NewEventDto event, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + id));
        Categories categories = categoriesRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + event.getCategory()));
        Event eventSave = EventsMapper.toEvent(event);
        eventSave.setInitiator(user);
        eventSave.setCategory(categories);
        return EventsMapper.toEventFullDto(repository.save(eventSave));
    }

    public EventFullDto update(NewEventDto event, Long userId, Long eventId) {
        Categories category = categoriesRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + event.getCategory()));
        Event eventToUpdate = repository.findFirstByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("event or user not found by id: " + eventId + "," + userId));
        Event eventSave = EventsMapper.toUpdate(eventToUpdate, event, category);
        return EventsMapper.toEventFullDto(repository.save(eventSave));
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

    public List<EventShortDto> getAll(EventFilterModel filter) {
        EventPredicatesBuilder builder = new EventPredicatesBuilder();
        Optional.ofNullable(filter.getText())
                .ifPresent(o1 -> builder.with("text", filter.getText()));
        Optional.ofNullable(filter.getCategories())
                .ifPresent(o1 -> builder.with("category", filter.getCategories()));
        Optional.ofNullable(filter.getPaid())
                .ifPresent(o1 -> builder.with("paid", filter.getPaid()));
        Optional.ofNullable(filter.getRangeStart())
                .ifPresent(o1 -> builder.with("eventStart", filter.getRangeStart()));
        Optional.ofNullable(filter.getRangeEnd())
                .ifPresent(o1 -> builder.with("eventEnd", filter.getRangeEnd()));
        Optional.ofNullable(filter.getOnlyAvailable())
                .ifPresent(o1 -> builder.with("available", filter.getOnlyAvailable()));
        BooleanExpression expression = builder.build();
        return repository.findAll(expression, filter.getPageable())
                .stream()
                .map(EventsMapper::toGetAll)
                .collect(Collectors.toList());
    }

    public EventFullDto get(Long eventId) {
        return EventsMapper.toEventFullDto(
                repository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId))
        );
    }

    public List<EventShortDto> getAllAdmin(EventFilterModel filter) {
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
        return repository.findAll(expression, filter.getPageable())
                .stream()
                .map(EventsMapper::toGetAll)
                .collect(Collectors.toList());
    }

    public EventFullDto updateAdmin(NewEventDto event, Long eventId) {
        Categories category = categoriesRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + event.getCategory()));
        Event eventToUpdate = repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId));
        Event eventSave = EventsMapper.toUpdate(eventToUpdate, event, category);
        return EventsMapper.toEventFullDto(repository.save(eventSave));
    }



}
