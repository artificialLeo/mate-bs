package com.book.store.controller;

import com.book.store.dto.CartItemResponseDto;
import com.book.store.dto.ShoppingCartRequestDto;
import com.book.store.dto.ShoppingCartResponseDto;
import com.book.store.dto.UpdateQuantityRequestDto;
import com.book.store.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get user's shopping cart by ID")
    @GetMapping
    public ResponseEntity<ShoppingCartResponseDto> getUserShoppingCart(Long userId) {
        ShoppingCartResponseDto shoppingCart = shoppingCartService.getUserShoppingCart(userId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shoppingCart);
    }

    @Operation(summary = "Add book to the shopping cart")
    @PostMapping
    public ResponseEntity<ShoppingCartResponseDto> addBookToCart(
            @Valid @RequestBody ShoppingCartRequestDto request,
            Long userId
    ) {
        ShoppingCartResponseDto shoppingCart = shoppingCartService
                .addBookToCart(userId, request.getBookId(), request.getQuantity());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shoppingCart);
    }

    @Operation(summary = "Update quantity of a book in the shopping cart")
    @PutMapping("/cart-items/{cartItemId}")
    public ResponseEntity<CartItemResponseDto> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateQuantityRequestDto request
    ) {
        CartItemResponseDto shoppingCart = shoppingCartService
                .updateCartItemQuantity(cartItemId, request.getQuantity());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(shoppingCart);
    }

    @Operation(summary = "Remove a book from the shopping cart")
    @DeleteMapping("/cart-items/{cartItemId}")
    public ResponseEntity<Void> removeBookFromCart(@PathVariable Long cartItemId) {
        shoppingCartService.removeBookFromCart(cartItemId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}
