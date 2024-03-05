package ru.practicum.exploreWithMe.stats.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.exploreWithMe.stats.events.model.Location;
import ru.practicum.exploreWithMe.stats.statuses.UserStateAction;

import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    private String description;
    private String annotation;
    private Long category;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private UserStateAction stateAction;
    private String title;
}
