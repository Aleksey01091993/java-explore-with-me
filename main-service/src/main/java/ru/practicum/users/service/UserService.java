package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.users.dto.RequestUserDto;
import ru.practicum.users.dto.ResponseUserDto;
import ru.practicum.users.mapper.UserMapper;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public ResponseUserDto add(RequestUserDto request) {
        return UserMapper.toResponse(repository.save(UserMapper.toUser(request)));
    }

    public ResponseUserDto delete(Long userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId));
        repository.delete(user);
        return UserMapper.toResponse(user);
    }

    public ResponseUserDto get(Long userId) {
        return UserMapper.toResponse(repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user not found by id: " + userId)));
    }

    public List<ResponseUserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toResponse)
                .collect(Collectors.toList());
    }
}
