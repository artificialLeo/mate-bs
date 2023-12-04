package com.book.store.controller;

import com.book.store.dto.CreateUserRequest;
import com.book.store.dto.CreateUserResponse;
import com.book.store.exception.RegistrationException;
import com.book.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    @PreAuthorize("permitAll()")
    @Operation(summary = "New user creation")
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody CreateUserRequest request) throws RegistrationException {
        CreateUserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

