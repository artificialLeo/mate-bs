package com.book.store.service.impl;

import com.book.store.dto.CreateUserRequest;
import com.book.store.dto.CreateUserResponse;
import com.book.store.exception.RegistrationException;
import com.book.store.mapper.UserMapper;
import com.book.store.model.User;
import com.book.store.repo.UserRepository;
import com.book.store.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CreateUserResponse registerUser(CreateUserRequest request) throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email is already in use");
        }

        User user = userMapper.toUser(request);
        User savedUser = userRepository.save(user);

        return userMapper.toCreateUserResponse(savedUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}

