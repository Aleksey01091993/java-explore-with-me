package ru.practicum.mapper;



import ru.practicum.dto.RequestStatsDto;
import ru.practicum.dto.ResponseGetStatsDto;
import ru.practicum.dto.ResponseStatsDto;
import ru.practicum.model.Stats;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MapperToStats {
    public static Stats toStats(RequestStatsDto requestStatsDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Stats.builder()
                .app(requestStatsDto.getApp())
                .uri(requestStatsDto.getUri())
                .ip(requestStatsDto.getIp())
                .timesTamp(LocalDateTime.parse(requestStatsDto.getTimestamp(), formatter))
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

    public static List<ResponseGetStatsDto> toResponseGetStats(List<Stats> stats, Boolean in) {
        List<ResponseGetStatsDto> responseGetStatsDto = new ArrayList<>();
        Map<String, List<Stats>> map = new HashMap<>();
        Map<String, Set<String>> setMap = new HashMap<>();
        if (in) {
            for (Stats u:stats) {
                if (setMap.containsKey(u.getUri())) {
                    setMap.get(u.getUri()).add(u.getIp());
                } else {
                    setMap.put(u.getUri(), new HashSet<>());
                    setMap.get(u.getUri()).add(u.getIp());
                }
            }
            for (String key: setMap.keySet()) {
                responseGetStatsDto.add(
                        ResponseGetStatsDto.builder()
                                .uri(key)
                                .hits(setMap.get(key).size())
                                .app(stats.get(0).getApp())
                                .build()
                );
            }
        } else {
            for (Stats s: stats) {
                if (map.containsKey(s.getUri())) {
                    map.get(s.getUri()).add(s);
                } else {
                    map.put(s.getUri(), new ArrayList<>());
                    map.get(s.getUri()).add(s);
                }
            }
            for (String key: map.keySet()) {
                responseGetStatsDto.add(
                        ResponseGetStatsDto.builder()
                                .uri(key)
                                .hits(map.get(key).size())
                                .app(map.get(key).get(0).getApp())
                                .build()
                );
            }
        }
        return responseGetStatsDto.stream()
                .sorted((o1, o2) -> o2.getHits() - o1.getHits())
                .collect(Collectors.toList());
    }
}
