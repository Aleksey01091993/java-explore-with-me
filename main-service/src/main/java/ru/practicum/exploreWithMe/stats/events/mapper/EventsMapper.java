package ru.practicum.exploreWithMe.stats.events.mapper;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.exploreWithMe.stats.categories.mapper.MapperCategories;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.events.dto.*;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.statuses.StateAction;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.querydsl.EventFilterModel;
import ru.practicum.exploreWithMe.stats.users.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class EventsMapper {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EventFilterModel toEventFilter(
            String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
            Boolean onlyAvailable, String sort, Integer from, Integer size) {
        Sort orderBy;
        if (sort.equals("EVENT_DATE")) {
            orderBy = Sort.by("eventDate");
        } else if (sort.equals("VIEWS"))
            orderBy = Sort.by("views");
        else {
            orderBy = Sort.unsorted();
        }
        return EventFilterModel.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(LocalDateTime.parse(rangeStart, DTF))
                .rangeEnd(LocalDateTime.parse(rangeEnd, DTF))
                .onlyAvailable(onlyAvailable)
                .pageable(PageRequest.of(from / size, size, orderBy))
                .build();
    }
    public static EventFilterModel toEventFilterAdmin(List<String> states, List<Long> usersId,
            List<Long> categories, String rangeStart, String rangeEnd, Integer from, Integer size) {
        List<Status> statuses = states.stream()
                .map(o1 -> {
                    switch (o1) {
                        case "PENDING":
                            return Status.PENDING;
                        case "PUBLISHED":
                            return Status.PUBLISHED;
                        case "CANCELED":
                            return Status.CANCELED;
                        default: return null;
                    }
                })
                .collect(Collectors.toList());
        return EventFilterModel.builder()
                .statuses(statuses)
                .categories(categories)
                .usersId(usersId)
                .rangeStart(LocalDateTime.parse(rangeStart, DTF))
                .rangeEnd(LocalDateTime.parse(rangeEnd, DTF))
                .pageable(PageRequest.of(from / size, size))
                .build();
    }
    public static Event toCreate(NewEventDto event) {
        return Event.builder()
                .annotation(event.getAnnotation())
                .confirmedRequest(0)
                .createdOn(LocalDateTime.now())
                .description(event.getDescription())
                .eventDate(LocalDateTime.parse(event.getEventDate(), DTF))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(Status.PENDING)
                .title(event.getTitle())
                .views(0)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(MapperCategories.toCategoryDto(event.getCategory()))
                .confirmedRequest(event.getConfirmedRequest())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toUpdate(Event event, UpdateEventUserRequest newEventDto, Categories category) {
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), DTF));
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        return event;
    }
    public static Event toUpdate(Event event, UpdateEventAdminRequest newEventDto, Categories category) {
        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(), DTF));
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        if (newEventDto.getStateAction() == StateAction.PUBLISH_EVENT) {
            event.setState(Status.PUBLISHED);
        } else if (newEventDto.getStateAction() == StateAction.REJECT_EVENT) {
            event.setState(Status.REJECTED);
        } else if (newEventDto.getStateAction() == StateAction.CANCEL_REVIEW) {
            event.setState(Status.CANCELED);
        }
        return event;
    }

    public static EventShortDto toGetAll(Event events) {
        return EventShortDto.builder()
                .id(events.getId())
                .annotation(events.getAnnotation())
                .category(MapperCategories.toCategoryDto(events.getCategory()))
                .confirmedRequests(events.getConfirmedRequest())
                .eventDate(events.getEventDate())
                .initiator(UserMapper.toUserShortDto(events.getInitiator()))
                .paid(events.getPaid())
                .title(events.getTitle())
                .views(events.getViews())
                .build();
    }
}
