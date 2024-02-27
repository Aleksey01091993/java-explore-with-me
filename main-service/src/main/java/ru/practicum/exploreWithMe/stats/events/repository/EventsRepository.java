package ru.practicum.exploreWithMe.stats.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.model.QEvent;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventsRepository extends JpaRepository<Event, Long>,
        QuerydslPredicateExecutor<Event>,
        QuerydslBinderCustomizer<QEvent> {
    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Optional<Event> findFirstByIdAndInitiator_Id(Long id, Long userId);
    List<Event> findAllByIdIn(List<Long> eventId);
}
