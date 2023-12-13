package com.book.store.dto;

import lombok.Data;

@Data
public class CreateUserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
