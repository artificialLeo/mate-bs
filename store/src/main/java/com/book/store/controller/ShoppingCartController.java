package com.book.store.controller;

import com.book.store.dto.ShoppingCartRequestDto;
import com.book.store.dto.UpdateQuantityRequestDto;
import com.book.store.exception.BookNotFoundException;
import com.book.store.exception.InvalidRequestException;
import com.book.store.model.ShoppingCart;
import com.book.store.service.ShoppingCartService;
import com.book.store.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    private final UserService userService;

    @Operation(summary = "Get user's shopping cart by ID")
    @GetMapping
    public ResponseEntity<ShoppingCart> getUserShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartService.getUserShoppingCart(userId);

        if (shoppingCart != null) {
            return ResponseEntity.ok(shoppingCart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add book to the shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @PostMapping
    public ResponseEntity<ShoppingCart> addBookToCart(
            @Valid @RequestBody ShoppingCartRequestDto request,
            Long userId
    ) {
        try {
            ShoppingCart shoppingCart = shoppingCartService
                    .addBookToCart(userId, request.getBookId(), request.getQuantity());

            return ResponseEntity.ok(shoppingCart);
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Update quantity of a book in the shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantity updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @PutMapping("/cart-items/{cartItemId}")
    public ResponseEntity<ShoppingCart> updateCartItemQuantity(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateQuantityRequestDto request
    ) {
        try {
            ShoppingCart shoppingCart = shoppingCartService
                    .updateCartItemQuantity(cartItemId, request.getQuantity());

            return ResponseEntity.ok(shoppingCart);
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Remove a book from the shopping cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book removed successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping("/cart-items/{cartItemId}")
    public ResponseEntity<Void> removeBookFromCart(@PathVariable Long cartItemId) {
        try {
            shoppingCartService.removeBookFromCart(cartItemId);

            return ResponseEntity.ok().build();
        } catch (BookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
