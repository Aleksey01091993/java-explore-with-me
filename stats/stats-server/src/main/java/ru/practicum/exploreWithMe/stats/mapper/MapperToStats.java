package ru.practicum.exploreWithMe.stats.mapper;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import ru.practicum.exploreWithMe.stats.dto.RequestStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseGetStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseStatsDto;
import ru.practicum.exploreWithMe.stats.model.QStats;
import ru.practicum.exploreWithMe.stats.model.Stats;
import ru.practicum.exploreWithMe.stats.model.StatsUnique;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MapperToStats {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static Stats toStats(RequestStatsDto requestStatsDto) {
        return Stats.builder()
                .app(requestStatsDto.getApp())
                .uri(requestStatsDto.getUri())
                .ip(requestStatsDto.getIp())
                .timesTamp(LocalDateTime.parse(requestStatsDto.getTimestamp(), DTF))
                .build();
    }

    public static ResponseStatsDto toResponseStats(Stats responseStatsDto) {
        return ResponseStatsDto.builder()
                .id(responseStatsDto.getId())
                .app(responseStatsDto.getApp())
                .uri(responseStatsDto.getUri())
                .ip(responseStatsDto.getIp())
                .timesTamp(responseStatsDto.getTimesTamp())
                .build();
    }

    public static StatsUnique toStatsUnique(RequestStatsDto requestStatsDto) {
        return StatsUnique.builder()
                .app(requestStatsDto.getApp())
                .uriIp(requestStatsDto.getUri() + "#" + requestStatsDto.getIp())
                .uri(requestStatsDto.getUri())
                .timesTamp(LocalDateTime.parse(requestStatsDto.getTimestamp(), DTF))
                .build();
    }

    public static List<ResponseGetStatsDto> toResponseGetStatsUnique(Iterable<StatsUnique> statsUniques) {
        List<ResponseGetStatsDto> response = new ArrayList<>();
        Map<String, Integer> mapStats = new HashMap<>();
        for (StatsUnique unique : statsUniques) {
            String key = unique.getUri();
            if (mapStats.containsKey(key)) {
                mapStats.put(key, mapStats.get(key) + 1);
            } else {
                mapStats.put(key, 1);
            }
        }
        for (String key: mapStats.keySet()) {
            response.add(
                    ResponseGetStatsDto.builder()
                            .app("ewm-main-service")
                            .uri(key)
                            .hits(mapStats.get(key))
                            .build()
            );
        }
        return response.stream()
                .sorted((o1, o2) -> o2.getHits() - o1.getHits())
                .collect(Collectors.toList());
    }

    public static List<ResponseGetStatsDto> toResponseGetStats(Iterable<Stats> stats) {
        List<ResponseGetStatsDto> response = new ArrayList<>();
        Map<String, Integer> mapStats = new HashMap<>();
        for (Stats keyStats : stats) {
            String key = keyStats.getUri();
            if (mapStats.containsKey(key)) {
                mapStats.put(key, mapStats.get(key) + 1);
            } else {
                mapStats.put(key, 1);
            }
        }
        for (String key: mapStats.keySet()) {
            response.add(
                    ResponseGetStatsDto.builder()
                            .app("ewm-main-service")
                            .uri(key)
                            .hits(mapStats.get(key))
                            .build()
            );
        }
        return response.stream()
                .sorted((o1, o2) -> o2.getHits() - o1.getHits())
                .collect(Collectors.toList());
    }



}
