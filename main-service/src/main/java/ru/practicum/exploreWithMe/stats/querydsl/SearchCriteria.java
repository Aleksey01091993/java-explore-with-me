package ru.practicum.exploreWithMe.stats.querydsl;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class SearchCriteria {
    private String key;
    private Object value;
}
