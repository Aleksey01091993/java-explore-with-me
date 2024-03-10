package ru.practicum.exploreWithMe.stats.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.dto.RequestStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseGetStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseStatsDto;
import ru.practicum.exploreWithMe.stats.mapper.MapperToStats;
import ru.practicum.exploreWithMe.stats.model.QStats;
import ru.practicum.exploreWithMe.stats.model.QStatsUnique;
import ru.practicum.exploreWithMe.stats.model.StatsUnique;
import ru.practicum.exploreWithMe.stats.repository.StatsRepository;
import ru.practicum.exploreWithMe.stats.repository.StatsUniqueRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsService {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsRepository repository;
    private final StatsUniqueRepository uniqueRepository;

    public ResponseStatsDto add(RequestStatsDto stats) {
        StatsUnique statsUnique = uniqueRepository
                .findByUriIp(stats.getUri() + "#" + stats.getIp()).orElse(null);
        if (statsUnique != null) {
            statsUnique.setTimesTamp(LocalDateTime.parse(stats.getTimestamp(), DTF));
            uniqueRepository.save(statsUnique);
            return MapperToStats.toResponseStats(repository.save(MapperToStats.toStats(stats)));
        } else {
            uniqueRepository.save(MapperToStats.toStatsUnique(stats));
            return MapperToStats.toResponseStats(repository.save(MapperToStats.toStats(stats)));
        }


    }

    public List<ResponseGetStatsDto> get
            (String strStart, String strEnd, List<String> uris, Boolean unique) {
        if (unique == null) {
            unique = false;
        }
        if (unique) {
            return MapperToStats
                    .toResponseGetStatsUnique(uniqueRepository.findAll(expression(strStart, strEnd, uris, unique)));
        } else {
            BooleanExpression expression = expression(strStart, strEnd, uris, unique);
            return MapperToStats.toResponseGetStats(repository.findAll(expression));
        }
    }

    private BooleanExpression expression(String startTime, String endTime, List<String> uris, Boolean unique) {
        LocalDateTime start = startTime == null ? null : LocalDateTime.parse(startTime, DTF);
        LocalDateTime end = endTime == null ? null : LocalDateTime.parse(endTime, DTF);
        List<BooleanExpression> expressions = new ArrayList<>();
        if (unique) {
            Optional.ofNullable(start).ifPresent(o1 -> expressions.add(QStatsUnique.statsUnique.timesTamp.after(o1)));
            Optional.ofNullable(end).ifPresent(o1 -> expressions.add(QStatsUnique.statsUnique.timesTamp.before(o1)));
            Optional.ofNullable(uris).ifPresent(o1 -> expressions.add(QStatsUnique.statsUnique.uri.in(o1)));
        } else {
            Optional.ofNullable(start).ifPresent(o1 -> expressions.add(QStats.stats.timesTamp.after(o1)));
            Optional.ofNullable(end).ifPresent(o1 -> expressions.add(QStats.stats.timesTamp.before(o1)));
            Optional.ofNullable(uris).ifPresent(o1 -> expressions.add(QStats.stats.uri.in(o1)));
        }

        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : expressions) {
            result = result.and(predicate);
        }
        return result;
    }
}