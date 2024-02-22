package ru.practicum.exploreWithMe.stats.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.events.model.Location;


import java.time.LocalDateTime;

@Data
@Builder
public class NewEventDto {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
