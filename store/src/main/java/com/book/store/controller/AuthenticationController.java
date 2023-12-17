package com.book.store.controller;

import com.book.store.dto.CreateUserRequestDto;
import com.book.store.dto.CreateUserResponseDto;
import com.book.store.dto.UserLoginRequestDto;
import com.book.store.dto.UserLoginResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.secutiry.AuthenticationService;
import com.book.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "New user creation")
    public ResponseEntity<CreateUserResponseDto> register(
            @Valid @RequestBody CreateUserRequestDto request
    ) throws RegistrationException {
        CreateUserResponseDto response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<UserLoginResponseDto> login(
            @Valid @RequestBody UserLoginRequestDto request
    ) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(authenticationService.authenticate(request));
    }
}
