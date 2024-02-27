package ru.practicum.exploreWithMe.stats.request.mapper;

import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.stats.request.model.Request;
import ru.practicum.exploreWithMe.stats.users.model.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static Request toCreate(User user, Event event) {
        return Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .status(Status.PENDING)
                .build();
    }

    public static ParticipationRequestDto toDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}
