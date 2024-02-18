package ru.practicum.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class RequestStatsDto {
    private String app;
    private String url;
    private String ip;
}
