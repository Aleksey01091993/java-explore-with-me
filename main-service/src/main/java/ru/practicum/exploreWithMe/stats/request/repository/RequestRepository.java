package ru.practicum.exploreWithMe.stats.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.exploreWithMe.stats.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findFirstByRequester_IdAndEvent_Id(Long userId, Long eventId);
    List<Request> findAllByRequester_Id(Long userId);
    List<Request> findAllByRequester_IdAndEvent_Id(Long userId, Long eventId);
}
