package ru.practicum.exploreWithMe.stats.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.model.QEvent;
import ru.practicum.exploreWithMe.stats.statuses.Status;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventsRepository extends JpaRepository<Event, Long>,
        JpaSpecificationExecutor<Event>,
        QuerydslPredicateExecutor<Event>,
        QuerydslBinderCustomizer<QEvent> {
    @Override
    default void customize(final QuerydslBindings bindings, final QEvent root) {
    }
    List<Event> findAllByInitiator_Id(Long userId, Pageable pageable);

    Optional<Event> findFirstByIdAndInitiator_Id(Long id, Long userId);
    List<Event> findAllByIdIn(List<Long> eventId);

    Optional<Event> findFirstByIdAndState(Long id, Status status);
}
