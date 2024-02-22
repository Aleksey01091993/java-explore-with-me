package ru.practicum.exploreWithMe.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class RequestStatsDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
