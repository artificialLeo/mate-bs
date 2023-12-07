package com.book.store.controller;

import com.book.store.dto.CreateUserRequest;
import com.book.store.dto.CreateUserResponse;
import com.book.store.dto.UserLoginRequestDto;
import com.book.store.dto.UserLoginResponseDto;
import com.book.store.exception.RegistrationException;
import com.book.store.secutiry.AuthenticationService;
import com.book.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;


    @PostMapping("/registration")
    @Operation(summary = "New user creation")
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody CreateUserRequest request) throws RegistrationException {
        CreateUserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto request) {
        return new ResponseEntity<>(authenticationService.authenticate(request), HttpStatus.ACCEPTED);
    }

}

