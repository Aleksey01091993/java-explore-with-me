package ru.practicum.exploreWithMe.stats.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.exploreWithMe.stats.events.model.Location;
import ru.practicum.exploreWithMe.stats.statuses.UserStateAction;

import javax.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    @Length(min = 20, max = 7000)
    private String description;
    @Length(min = 20, max = 2000)
    private String annotation;
    private Long category;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private UserStateAction stateAction;
    @Length(min = 3, max = 120)
    private String title;
}
