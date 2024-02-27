package ru.practicum.exploreWithMe.stats.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.model.Status;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.ConflictError;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;
import ru.practicum.exploreWithMe.stats.request.dto.ParticipationRequestDto;
import ru.practicum.exploreWithMe.stats.request.mapper.RequestMapper;
import ru.practicum.exploreWithMe.stats.request.model.Request;
import ru.practicum.exploreWithMe.stats.request.repository.RequestRepository;
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestService {
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;
    private final RequestRepository repository;

    public ParticipationRequestDto create(Long eventId, Long userId) {
        Request request = repository.findFirstByRequester_IdAndEvent_Id(userId, eventId).orElse(null);
        if (request != null) {
            throw new ConflictError("such a request already exists");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event not found by id: " + eventId));
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictError("the initiator of the event cannot add a request to participate in his event");
        }
        return RequestMapper.toDto(repository.save(RequestMapper.toCreate(user, event)));
    }

    public ParticipationRequestDto canceled(Long eventId, Long userId) {
        Request request = repository.findFirstByRequester_IdAndEvent_Id(userId, eventId)
                .orElseThrow(() -> new NotFoundException("event or user not found by id: " + eventId + "," + userId));
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
        return repository.findAllByRequester_IdAndEvent_Id(userId, eventId).stream()
                .map(RequestMapper::toDto).collect(Collectors.toList());
    }


}
