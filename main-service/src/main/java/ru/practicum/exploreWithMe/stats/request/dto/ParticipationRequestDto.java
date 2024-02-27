package ru.practicum.exploreWithMe.stats.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.statuses.Status;


import java.time.LocalDateTime;

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private Status status;
}
