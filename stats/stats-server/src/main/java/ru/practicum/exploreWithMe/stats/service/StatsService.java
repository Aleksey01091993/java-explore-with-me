package ru.practicum.exploreWithMe.stats.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.dto.RequestStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseGetStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseStatsDto;
import ru.practicum.exploreWithMe.stats.mapper.MapperToStats;
import ru.practicum.exploreWithMe.stats.model.QStats;
import ru.practicum.exploreWithMe.stats.model.QStatsUnique;
import ru.practicum.exploreWithMe.stats.model.Stats;
import ru.practicum.exploreWithMe.stats.model.StatsUnique;
import ru.practicum.exploreWithMe.stats.repository.StatsRepository;
import ru.practicum.exploreWithMe.stats.repository.StatsUniqueRepository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsRepository repository;
    private final StatsUniqueRepository uniqueRepository;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
            return MapperToStats.toResponseGetStats(repository.findAll(expression(strStart, strEnd, uris, unique)));
        }
    }

    private BooleanExpression expression(String startTime, String endTime, List<String> uris, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(startTime, DTF);
        LocalDateTime end = LocalDateTime.parse(endTime, DTF);
        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        if (unique) {
            Optional.ofNullable(start).ifPresent(o1 -> result.and(QStatsUnique.statsUnique.timesTamp.before(o1)));
            Optional.ofNullable(end).ifPresent(o1 -> result.and(QStatsUnique.statsUnique.timesTamp.after(o1)));
            Optional.ofNullable(uris).ifPresent(o1 -> result.and(QStatsUnique.statsUnique.uri.in(uris)));
            return result;
        } else {
            Optional.ofNullable(start).ifPresent(o1 -> result.and(QStats.stats.timesTamp.before(o1)));
            Optional.ofNullable(end).ifPresent(o1 -> result.and(QStats.stats.timesTamp.after(o1)));
            Optional.ofNullable(uris).ifPresent(o1 -> result.and(QStats.stats.uri.in(o1)));
            return result;
        }

    }
}
