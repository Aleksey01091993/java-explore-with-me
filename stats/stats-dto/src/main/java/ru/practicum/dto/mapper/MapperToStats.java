package ru.practicum.dto.mapper;

import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseGetStatsDto;
import ru.practicum.dto.ResponseStatsDto;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapperToStats {
    public static Stats toStats(RequestStatsDto requestStatsDto) {
        return Stats.builder()
                .app(requestStatsDto.getApp())
                .url(requestStatsDto.getUrl())
                .ip(requestStatsDto.getIp())
                .timesTamp(LocalDateTime.now())
                .build();
    }

    public static ResponseStatsDto toResponseStats(Stats responseStatsDto) {
        return ResponseStatsDto.builder()
                .id(responseStatsDto.getId())
                .app(responseStatsDto.getApp())
                .url(responseStatsDto.getUrl())
                .ip(responseStatsDto.getIp())
                .timesTamp(responseStatsDto.getTimesTamp())
                .build();
    }

    public static List<ResponseGetStatsDto> toResponseGetStats(List<Stats> stats) {
        List<ResponseGetStatsDto> responseGetStatsDto = new ArrayList<>();
        Map<String, List<Stats>> map = new HashMap<>();
        for (Stats s: stats) {
            if (map.containsKey(s.getUrl())) {
                map.get(s.getUrl()).add(s);
            } else {
                map.put(s.getUrl(), new ArrayList<>());
            }
        }
        for (String key: map.keySet()) {
            responseGetStatsDto.add(
                    ResponseGetStatsDto.builder()
                            .url(key)
                            .hits(map.get(key).size())
                            .app(map.get(key).get(0).getApp())
                            .build()
            );
        }
        return responseGetStatsDto;
    }
}
