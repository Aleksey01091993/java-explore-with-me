package ru.practicum.exploreWithMe.stats.compilations.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @Length(max = 50)
    private String title;
}
