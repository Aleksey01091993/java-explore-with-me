package ru.practicum.exploreWithMe.stats.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Data
@Builder
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Float lat;
    private Float lon;
}
