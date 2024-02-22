package ru.practicum.exploreWithMe.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.dto.RequestStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseGetStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseStatsDto;
import ru.practicum.exploreWithMe.stats.mapper.MapperToStats;
import ru.practicum.exploreWithMe.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repository;

    public ResponseStatsDto add(RequestStatsDto stats) {
        return MapperToStats.toResponseStats(repository.save(MapperToStats.toStats(stats)));
    }

    public List<ResponseGetStatsDto> get
            (String strStart, String strEnd, List<String> uris, Boolean unique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        final LocalDateTime start = LocalDateTime.parse(strStart, formatter);
        final LocalDateTime end = LocalDateTime.parse(strEnd, formatter);
        if (unique == null) {
            unique = false;
        }
        if (uris == null && !unique) {
            return MapperToStats.toResponseGetStats(repository.findByTimesTampBetween(start, end), unique);
        } else if (uris == null) {
            return MapperToStats.toResponseGetStats(repository.findDistinctByTimesTampBetween(start, end), unique);
        } else if (unique) {
            return MapperToStats.toResponseGetStats(repository.findDistinctByUriInAndTimesTampBetween(uris, start, end), unique);
        } else {
            return MapperToStats.toResponseGetStats(repository.findByTimesTampBetweenAndUriIn(start, end, uris), unique);
        }
    }
}
