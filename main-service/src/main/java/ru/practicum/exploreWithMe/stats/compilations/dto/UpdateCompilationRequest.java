package ru.practicum.exploreWithMe.stats.compilations.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.events.dto.EventShortDto;

import java.util.List;

@Data
@Builder
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
