package com.book.store.dto;

import lombok.Data;

@Data
public class ShoppingCartRequestDto {
    private Long bookId;
    private int quantity;
}
