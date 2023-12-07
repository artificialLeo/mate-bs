package com.book.store.dto;

import com.book.store.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "repeatPassword", message = "The password fields must match")
})
public class CreateUserRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String repeatPassword;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String shippingAddress;
}

