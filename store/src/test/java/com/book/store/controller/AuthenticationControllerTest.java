package com.book.store.controller;

import com.book.store.dto.CreateUserRequestDto;
import com.book.store.dto.CreateUserResponseDto;
import com.book.store.dto.UserLoginRequestDto;
import com.book.store.dto.UserLoginResponseDto;
import com.book.store.repo.UserRepository;
import com.book.store.secutiry.AuthenticationService;
import com.book.store.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("register -> Success")
    void register_ValidData_SuccessfulRegistration() throws Exception {
        CreateUserRequestDto createUserRequestDto = new CreateUserRequestDto(
                "test@example.com",
                "password",
                "password",
                "John",
                "Doe",
                "123 Main St"
        );
        when(userService.registerUser(any())).thenReturn(new CreateUserResponseDto(
                1L,
                "test@example.com",
                "John",
                "Doe",
                "123 Main St"));

        ResultActions result = mockMvc.perform(post("/api/auth/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createUserRequestDto)));

        result.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("login -> Success")
    void login_ValidData_SuccessfulLogin() throws Exception {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto("test@example.com", "password");
        when(authenticationService.authenticate(any())).thenReturn(new UserLoginResponseDto("dummyToken"));

        ResultActions result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginRequestDto)));

        result.andExpect(status().isAccepted());
    }
}
