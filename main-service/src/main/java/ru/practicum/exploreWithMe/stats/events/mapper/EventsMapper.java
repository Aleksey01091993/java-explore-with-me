package ru.practicum.exploreWithMe.stats.events.mapper;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.exploreWithMe.stats.categories.mapper.MapperCategories;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.events.dto.EventFullDto;
import ru.practicum.exploreWithMe.stats.events.dto.EventShortDto;
import ru.practicum.exploreWithMe.stats.events.dto.NewEventDto;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.querydsl.EventFilterModel;
import ru.practicum.exploreWithMe.stats.users.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    public static Event toEvent(NewEventDto event) {
        return Event.builder()
                .annotation(event.getAnnotation())
                .createdOn(LocalDateTime.now())
                .description(event.getDescription())
                .eventDate(LocalDateTime.parse(event.getEventDate(), DTF))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
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

    public static Event toUpdate(Event event, NewEventDto newEventDto, Categories category) {
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
