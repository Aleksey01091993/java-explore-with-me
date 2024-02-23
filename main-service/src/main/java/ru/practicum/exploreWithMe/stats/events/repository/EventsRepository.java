package ru.practicum.exploreWithMe.stats.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.events.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Optional<Event> findFirstByIdAndInitiator_Id(Long id, Long userId);

    List<Event> findAllByDescriptionContainingIgnoreCaseOrAnnotationContainingIgnoreCaseAndCategory_IdInAndPaidAndEventDateBeforeAndEventDateAfter
            (String description, String annotation, List<Long> categoryId, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable, Sort sort);

    List<Event> findAllByDescriptionContainingIgnoreCaseOrAnnotationContainingIgnoreCaseAndCategory_IdInAndEventDateBeforeAndEventDateAfter
            (String description, String annotation, List<Long> categoryId, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable, Sort sort);
}
