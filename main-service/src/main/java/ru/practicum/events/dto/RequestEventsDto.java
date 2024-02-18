package ru.practicum.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.events.model.Location;

import java.time.LocalDateTime;

@Data
@Builder
public class RequestEventsDto {
    private String annotation;
    private Integer category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;

}
