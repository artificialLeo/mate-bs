package com.book.store.service;

import com.book.store.dto.CreateUserRequestDto;
import com.book.store.dto.CreateUserResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.mapper.UserMapper;
import com.book.store.model.User;
import com.book.store.repo.UserRepository;
import com.book.store.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should register a new user")
    void registerUser_Success() throws RegistrationException {
        CreateUserRequestDto request = new CreateUserRequestDto();
        request.setEmail("test@example.com");
        User user = new User();
        user.setEmail(request.getEmail());
        CreateUserResponseDto expectedResponse = new CreateUserResponseDto();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userMapper.toUser(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toCreateUserResponse(user)).thenReturn(expectedResponse);

        CreateUserResponseDto response = userService.registerUser(request);

        assertNotNull(response);
        assertEquals(expectedResponse, response);

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, times(1)).save(user);
        verify(userMapper, times(1)).toCreateUserResponse(user);
    }

    @Test
    @DisplayName("Should throw RegistrationException when email is already in use")
    void registerUser_EmailInUse_ThrowRegistrationException() {
        CreateUserRequestDto request = new CreateUserRequestDto();
        request.setEmail("test@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(RegistrationException.class, () -> userService.registerUser(request));

        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any());
        verify(userMapper, never()).toCreateUserResponse(any());
    }
}

