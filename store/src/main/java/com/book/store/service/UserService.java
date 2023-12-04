package com.book.store.service;

import com.book.store.dto.CreateUserRequest;
import com.book.store.dto.CreateUserResponse;
import com.book.store.model.User;

import java.util.Optional;

public interface UserService {
    CreateUserResponse registerUser(CreateUserRequest request);

    Optional<User> findByEmail(String email);
}
