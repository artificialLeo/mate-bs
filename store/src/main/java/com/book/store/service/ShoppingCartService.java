package com.book.store.service;

import com.book.store.dto.CartItemResponseDto;
import com.book.store.dto.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getUserShoppingCart(Long userId);

    ShoppingCartResponseDto addBookToCart(Long userId, Long bookId, int quantity);

    CartItemResponseDto updateCartItemQuantity(Long cartItemId, int quantity);

    void removeBookFromCart(Long cartItemId);
}
