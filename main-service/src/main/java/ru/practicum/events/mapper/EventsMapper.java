package ru.practicum.events.mapper;

import ru.practicum.categories.model.Categories;
import ru.practicum.events.dto.RequestEventsDto;
import ru.practicum.events.model.Events;
import ru.practicum.events.model.Status;
import ru.practicum.users.model.User;

import java.time.LocalDateTime;

public class EventsMapper {
    public static Events toEvent(
            RequestEventsDto request,
            User user,
            Categories categories,
            Integer views,
            Status state,
            Integer confirmedRequest,
            Boolean requestModeration
    ) {
        return Events.builder()
                .annotation(request.getAnnotation())
                .category(categories)
                .confirmedRequest(confirmedRequest)
                .createdOn(LocalDateTime.now())
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .initiator(user)
                .location(request.getLocation())
                .paid(request.getPaid())
                .participantLimit(request.getParticipantLimit())
                .requestModeration(requestModeration)
                .state(state)
                .title(request.getTitle())
                .views(views)
                .build();
    }
}
