package ru.practicum.exploreWithMe.stats.events.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventRequestStatusUpdateResult {
    private ParticipationRequestDto confirmedRequests;
    private ParticipationRequestDto rejectedRequests;
}