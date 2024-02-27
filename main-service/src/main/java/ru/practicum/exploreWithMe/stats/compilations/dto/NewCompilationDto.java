package ru.practicum.exploreWithMe.stats.compilations.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotBlank
    @NotNull
    private String title;
}
