package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class ResponseGetStatsDto {
    private String app;
    private String uri;
    private Integer hits;

}
