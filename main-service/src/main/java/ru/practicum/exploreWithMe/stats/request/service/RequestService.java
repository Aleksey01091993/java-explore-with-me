package ru.practicum.exploreWithMe.stats.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.ConflictError;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;
import ru.practicum.exploreWithMe.stats.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.exploreWithMe.stats.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.exploreWithMe.stats.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.stats.request.mapper.RequestMapper;
import ru.practicum.exploreWithMe.stats.request.model.Request;
import ru.practicum.exploreWithMe.stats.request.repository.RequestRepository;
import ru.practicum.exploreWithMe.stats.statuses.Status;
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;
    private final RequestRepository repository;

    public ParticipationRequestDto create(Long userId, Long eventId) {
        Request request = repository.findFirstByRequester_IdAndEvent_Id(userId, eventId).orElse(null);
        if (request != null) {
            throw new ConflictError("such a request already exists");
        }
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId));
        if (!(event.getState() == Status.PUBLISHED)) {
            throw new ConflictError("Adding a request to participate in an unpublished event");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictError("the initiator of the event cannot add a request to participate in his event");
        }
        if ((Objects.equals(event.getConfirmedRequest(), event.getParticipantLimit()) &&
                event.getParticipantLimit() != 0)) {
            throw new ConflictError("The limit of participation requests has been reached");
        }
        Request requestCreate = RequestMapper.toCreate(user, event);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            requestCreate.setStatus(Status.CONFIRMED);
            event.setConfirmedRequest(event.getConfirmedRequest() + 1);
            eventsRepository.save(event);
        }
        return RequestMapper.toDto(repository.save(requestCreate));
    }

    public ParticipationRequestDto canceled(Long userId, Long requestId) {
        Request request = repository.findFirstByIdAndRequester_Id(requestId, userId)
                .orElseThrow(() -> new NotFoundException("requestId or userId not found by request: " + requestId + "," + userId));
        if (request.getStatus() == Status.CONFIRMED) {
            request.getEvent().setConfirmedRequest(request.getEvent().getConfirmedRequest() - 1);
            eventsRepository.save(request.getEvent());
        }
        request.setStatus(Status.CANCELED);
        return RequestMapper.toDto(repository.save(request));
    }

    public List<ParticipationRequestDto> getAll(Long userId) {
        return repository.findAllByRequester_Id(userId).stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    public List<ParticipationRequestDto> getAllUser(Long userId, Long eventId) {
        Event event = eventsRepository.findFirstByIdAndInitiator_Id(eventId, userId).orElse(null);
        if (event == null) {
            return Collections.emptyList();
        }
        return repository.findAllByEvent_Id(eventId).stream()
                .map(RequestMapper::toDto).collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult update(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        Event event = eventsRepository.findFirstByIdAndInitiator_Id(eventId, userId)
                .orElseThrow(() -> new NotFoundException("user or event not found by id: " + userId + "," + eventId));
        if (Objects.equals(event.getConfirmedRequest(), event.getParticipantLimit())) {
            throw new ConflictError("The limit of participation requests has been reached");
        }
        List<Request> search = repository.findAllByIdIn(request.getRequestIds());
        for (Request req : search) {
            if (req.getStatus() != Status.PENDING) {
                throw new ConflictError("The status can only be changed for pending applications");
            }
        }
        List<Request> save = new ArrayList<>();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        for (Request value : search) {
            if (request.getStatus() == Status.REJECTED) {
                value.setStatus(request.getStatus());
                save.add(value);
                rejectedRequests.add(RequestMapper.toDto(value));
            } else if (request.getStatus() == Status.CONFIRMED) {
                if (!Objects.equals(event.getConfirmedRequest(), event.getParticipantLimit())) {
                    value.setStatus(request.getStatus());
                    save.add(value);
                    confirmedRequests.add(RequestMapper.toDto(value));
                    event.setConfirmedRequest(event.getConfirmedRequest() + 1);
                } else {
                    value.setStatus(Status.REJECTED);
                    rejectedRequests.add(RequestMapper.toDto(value));
                    save.add(value);
                }
            }
        }
        eventsRepository.save(event);
        repository.saveAll(save);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }
}
