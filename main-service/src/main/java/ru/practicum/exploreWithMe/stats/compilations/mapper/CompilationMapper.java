package ru.practicum.exploreWithMe.stats.compilations.mapper;

import ru.practicum.exploreWithMe.stats.compilations.dto.CompilationDto;
import ru.practicum.exploreWithMe.stats.compilations.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.stats.compilations.dto.UpdateCompilationRequest;
import ru.practicum.exploreWithMe.stats.compilations.model.Compilation;
import ru.practicum.exploreWithMe.stats.events.mapper.EventsMapper;
import ru.practicum.exploreWithMe.stats.events.model.Event;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation toCompilation(NewCompilationDto compilation, List<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents() == null ? Collections.emptyList() :
                        compilation.getEvents().stream()
                                .map(EventsMapper::toGetAll)
                                .collect(Collectors.toList())
                )
                .pinned(compilation.getPinned() != null && compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toUpdate(Compilation compilation, UpdateCompilationRequest dto, List<Event> events) {
        return Compilation.builder()
                .id(compilation.getId())
                .events(dto.getEvents() == null ? compilation.getEvents() : events)
                .pinned(dto.getPinned() == null ? compilation.getPinned() : dto.getPinned())
                .title(dto.getTitle() == null || dto.getTitle().isEmpty() ? compilation.getTitle() : dto.getTitle())
                .build();
    }
}
