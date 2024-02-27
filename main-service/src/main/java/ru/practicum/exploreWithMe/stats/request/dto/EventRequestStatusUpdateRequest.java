package ru.practicum.exploreWithMe.stats.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.statuses.Status;


import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private Status status;

}
