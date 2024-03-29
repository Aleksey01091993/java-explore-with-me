package ru.practicum.exploreWithMe.stats.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.compilations.dto.CompilationDto;
import ru.practicum.exploreWithMe.stats.compilations.dto.NewCompilationDto;
import ru.practicum.exploreWithMe.stats.compilations.dto.UpdateCompilationRequest;
import ru.practicum.exploreWithMe.stats.compilations.mapper.CompilationMapper;
import ru.practicum.exploreWithMe.stats.compilations.model.Compilation;
import ru.practicum.exploreWithMe.stats.compilations.repository.CompilationRepository;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {
    private final CompilationRepository repository;
    private final EventsRepository eventsRepository;

    public CompilationDto create(NewCompilationDto compilation) {
        List<Event> events;
        if (compilation.getEvents() == null) {
            events = null;
        } else if (compilation.getEvents().isEmpty()) {
            events = Collections.emptyList();
        } else {
            events = eventsRepository.findAllByIdIn(compilation.getEvents());
        }
        return CompilationMapper.toCompilationDto(repository.save(CompilationMapper.toCompilation(compilation, events)));
    }

    public CompilationDto update(UpdateCompilationRequest compilationNew, Long compilationId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("compilation not found by id: " + compilationId));
        List<Event> events;
        if (compilationNew.getEvents() == null) {
            events = null;
        } else if (compilationNew.getEvents().isEmpty()) {
            events = Collections.emptyList();
        } else {
            events = eventsRepository.findAllByIdIn(compilationNew.getEvents());
        }
        return CompilationMapper.toCompilationDto(repository.save(CompilationMapper
                .toUpdate(compilation, compilationNew, events)));
    }

    public void delete(Long compilationId) {
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("compilation not found by id: " + compilationId));
        repository.delete(compilation);
    }

    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        if (pinned == null) {
            return repository.findAll(PageRequest.of(from / size, size)).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        } else {
            return repository.findAllByPinned(pinned, PageRequest.of(from / size, size)).stream()
                    .map(CompilationMapper::toCompilationDto)
                    .collect(Collectors.toList());
        }

    }

    public CompilationDto get (Long compilationId){
        Compilation compilation = repository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("compilation not found by id: " + compilationId));
        return CompilationMapper.toCompilationDto(compilation);
    }


}
