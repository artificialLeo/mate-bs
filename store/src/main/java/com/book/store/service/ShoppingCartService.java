package com.book.store.service;

import com.book.store.model.ShoppingCart;

public interface ShoppingCartService {
    ShoppingCart getUserShoppingCart(Long userId);

    ShoppingCart addBookToCart(Long userId, Long bookId, int quantity);

    ShoppingCart updateCartItemQuantity(Long cartItemId, int quantity);

    void removeBookFromCart(Long cartItemId);
}
