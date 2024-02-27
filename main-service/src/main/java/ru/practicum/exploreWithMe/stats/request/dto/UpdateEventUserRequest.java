package ru.practicum.exploreWithMe.stats.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.events.model.Location;


import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventUserRequest {
    private String description;
    private String annotation;
    private Long category;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    private String title;
}
