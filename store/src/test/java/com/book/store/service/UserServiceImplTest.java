package com.book.store.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @DisplayName("registerUser -> Success")
    void registerUser_ValidData_Success() throws RegistrationException {
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
    @DisplayName("registerUser -> Email In Use -> Throw RegistrationException")
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
