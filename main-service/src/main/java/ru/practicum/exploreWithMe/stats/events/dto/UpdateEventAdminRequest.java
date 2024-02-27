package ru.practicum.exploreWithMe.stats.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.events.model.Location;
import ru.practicum.exploreWithMe.stats.statuses.StateAction;


import java.time.LocalDateTime;

@Data
@Builder
public class UpdateEventAdminRequest {
    private String description;
    private String annotation;
    private Long category;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    private String title;
}