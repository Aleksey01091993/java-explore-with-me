package ru.practicum.exploreWithMe.stats.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.categories.model.Categories;
import ru.practicum.exploreWithMe.stats.categories.repository.CategoriesRepository;
import ru.practicum.exploreWithMe.stats.events.dto.EventFullDto;
import ru.practicum.exploreWithMe.stats.events.dto.NewEventDto;
import ru.practicum.exploreWithMe.stats.events.mapper.EventsMapper;
import ru.practicum.exploreWithMe.stats.events.model.Event;
import ru.practicum.exploreWithMe.stats.events.repository.EventsRepository;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class EventService {
    private final EventsRepository repository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;

    public EventFullDto add(NewEventDto event, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + id));
        Categories categories = categoriesRepository.findById(event.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found by id: " + event.getCategory()));
        Event eventSave = EventsMapper.toEvent(event);
        eventSave.setInitiator(user);
        eventSave.setCategory(categories);
        return EventsMapper.toEventFullDto(repository.save(eventSave));
    }



}
