package ru.practicum.exploreWithMe.stats.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.statuses.Status;

@Data
@Builder
public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private Status status;
}
