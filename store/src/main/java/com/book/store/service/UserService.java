package com.book.store.service;

import com.book.store.dto.CreateUserRequestDto;
import com.book.store.dto.CreateUserResponseDto;

public interface UserService {
    CreateUserResponseDto registerUser(CreateUserRequestDto request);
}
