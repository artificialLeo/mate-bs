package com.book.store.dto;

import lombok.Data;

@Data
public class CreateUserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String shippingAddress;
}
