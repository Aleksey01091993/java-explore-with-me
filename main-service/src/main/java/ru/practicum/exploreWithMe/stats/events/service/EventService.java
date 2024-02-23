package ru.practicum.exploreWithMe.stats.events.service;

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
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventsRepository repository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public EventFullDto add(NewEventDto event, Long id) {
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

    public List<EventShortDto> getAll
            (String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
             Boolean onlyAvailable, String sort, Integer from, Integer size) {
        LocalDateTime start;
        LocalDateTime end;
        Sort orderBy;
        if (sort.equals("EVENT_DATE")) {
            orderBy = Sort.by(Sort.Direction.ASC, "eventDate");
        } else if (sort.equals("VIEWS"))
            orderBy = Sort.by(Sort.Direction.ASC, "views");
        else {
            orderBy = Sort.unsorted();
        }
        if (categories == null) {
            categories = categoriesRepository.findAll().stream()
                    .map(Categories::getId)
                    .collect(Collectors.toList());
        }
        if (rangeStart == null) {
            start = LocalDateTime.MIN;
        } else {
            start = LocalDateTime.parse(rangeStart, DTF);
        }
        if (rangeEnd == null) {
            end = LocalDateTime.MAX;
        } else {
            end = LocalDateTime.parse(rangeEnd, DTF);
        }
        if (onlyAvailable == null) {
            onlyAvailable = false;
        }
        if (onlyAvailable && paid == null) {
            return repository
                    .findAllByDescriptionContainingIgnoreCaseOrAnnotationContainingIgnoreCaseAndCategory_IdInAndEventDateBeforeAndEventDateAfter(
                            text, text, categories, start, end, PageRequest.of(from / size, size), orderBy
                    ).stream()
                    .filter(o1 -> o1.getConfirmedRequest() < o1.getParticipantLimit())
                    .map(EventsMapper::toGetAll)
                    .collect(Collectors.toList());
        } else if (!onlyAvailable && paid == null) {
            return repository
                    .findAllByDescriptionContainingIgnoreCaseOrAnnotationContainingIgnoreCaseAndCategory_IdInAndEventDateBeforeAndEventDateAfter(
                            text, text, categories, start, end, PageRequest.of(from / size, size), orderBy
                    ).stream()
                    .map(EventsMapper::toGetAll)
                    .collect(Collectors.toList());
        } else if (!onlyAvailable && paid) {
            return repository
                    .findAllByDescriptionContainingIgnoreCaseOrAnnotationContainingIgnoreCaseAndCategory_IdInAndPaidAndEventDateBeforeAndEventDateAfter(
                            text, text, categories, paid, start, end, PageRequest.of(from / size, size), orderBy
                    ).stream()
                    .map(EventsMapper::toGetAll)
                    .collect(Collectors.toList());
        } else {
            return repository
                    .findAllByDescriptionContainingIgnoreCaseOrAnnotationContainingIgnoreCaseAndCategory_IdInAndPaidAndEventDateBeforeAndEventDateAfter(
                            text, text, categories, paid, start, end, PageRequest.of(from / size, size), orderBy
                    ).stream()
                    .filter(o1 -> o1.getConfirmedRequest() < o1.getParticipantLimit())
                    .map(EventsMapper::toGetAll)
                    .collect(Collectors.toList());
        }
    }

    public EventFullDto get(Long eventId) {
        return EventsMapper.toEventFullDto(
                repository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId + "," + eventId))
        );
    }


}
