package ru.practicum.exploreWithMe.stats.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.exception.NotFoundException;
import ru.practicum.exploreWithMe.stats.users.dto.NewUserRequest;
import ru.practicum.exploreWithMe.stats.users.dto.UserDto;
import ru.practicum.exploreWithMe.stats.users.mapper.UserMapper;
import ru.practicum.exploreWithMe.stats.users.model.User;
import ru.practicum.exploreWithMe.stats.users.repository.UserRepository;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserDto add(NewUserRequest request) {
        return UserMapper.toResponse(repository.save(UserMapper.toUser(request)));
    }

    public UserDto delete(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        repository.delete(user);
        return UserMapper.toResponse(user);
    }

    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        return repository.findAllByIdIn(ids, PageRequest.of(from/size, from)).stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }
}
