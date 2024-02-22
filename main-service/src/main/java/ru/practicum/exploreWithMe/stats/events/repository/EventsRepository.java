package ru.practicum.exploreWithMe.stats.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.exploreWithMe.stats.events.model.Event;


@Repository
public interface EventsRepository extends JpaRepository<Event, Long> {
}
