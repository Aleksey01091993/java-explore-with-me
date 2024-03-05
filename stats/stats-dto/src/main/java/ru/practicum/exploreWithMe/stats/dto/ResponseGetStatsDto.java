package ru.practicum.exploreWithMe.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetStatsDto {
    private String app;
    private String uri;
    private Integer hits;

}
