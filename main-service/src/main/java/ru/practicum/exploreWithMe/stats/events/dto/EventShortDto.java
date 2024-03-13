package ru.practicum.exploreWithMe.stats.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.exploreWithMe.stats.categories.dto.CategoryDto;
import ru.practicum.exploreWithMe.stats.coments.dto.CommentResponse;
import ru.practicum.exploreWithMe.stats.users.dto.UserShortDto;


import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
    private List<CommentResponse> comments;
}
