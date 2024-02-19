package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class RequestStatsDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
