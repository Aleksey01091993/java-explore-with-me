package ru.practicum.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Stats;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {
    List<Stats> findByTimesTampBetweenAndUrlIn(LocalDateTime start, LocalDateTime end, List<String> url);
    List<Stats> findDistinctByUrlInAndTimesTampBetween(List<String> uri, LocalDateTime start, LocalDateTime end);
    List<Stats> findByTimesTampBetween(LocalDateTime start, LocalDateTime end);
    List<Stats> findDistinctByTimesTampBetween(LocalDateTime start, LocalDateTime end);
}
