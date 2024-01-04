package com.book.store.controller;

import com.book.store.dto.CreateUserRequestDto;
import com.book.store.dto.CreateUserResponseDto;
import com.book.store.dto.UserLoginRequestDto;
import com.book.store.dto.UserLoginResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.secutiry.AuthenticationService;
import com.book.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

//    @BeforeEach
//    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
//    }

    @Test
    @DisplayName("Should register a new user")
    void register_Success() throws Exception {
        // Given
        CreateUserRequestDto request = new CreateUserRequestDto();
        CreateUserResponseDto response = new CreateUserResponseDto();

        when(userService.registerUser(request)).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.yourJsonFieldHere").value("expectedValue"));

        // Verify interactions
        verify(userService, times(1)).registerUser(request);
    }

    @Test
    @DisplayName("Should handle RegistrationException when registering a user")
    void register_HandleRegistrationException() throws Exception {
        // Given
        CreateUserRequestDto request = new CreateUserRequestDto();

        when(userService.registerUser(request))
                .thenThrow(new RegistrationException("Email is already in use"));

        // When/Then
        mockMvc.perform(post("/api/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Email is already in use"));

        // Verify interactions
        verify(userService, times(1)).registerUser(request);
    }

    @Test
    @DisplayName("Should login a user")
    void login_Success() throws Exception {
        // Given
        UserLoginRequestDto request = new UserLoginRequestDto("test@example.com", "password");
        UserLoginResponseDto response = new UserLoginResponseDto("yourTokenValueHere");

        when(authenticationService.authenticate(request)).thenReturn(response);

        // When/Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.yourJsonFieldHere").value("expectedValue"));

        // Verify interactions
        verify(authenticationService, times(1)).authenticate(request);
    }
}
