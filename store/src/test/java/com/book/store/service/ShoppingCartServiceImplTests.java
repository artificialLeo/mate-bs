package com.book.store.service;

import com.book.store.dto.CartItemResponseDto;
import com.book.store.dto.ShoppingCartResponseDto;
import com.book.store.mapper.ShoppingCartMapper;
import com.book.store.model.Book;
import com.book.store.model.CartItem;
import com.book.store.model.ShoppingCart;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CartItemRepository;
import com.book.store.repo.ShoppingCartRepository;
import com.book.store.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ShoppingCartServiceImplTests {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should get user shopping cart")
    void getUserShoppingCart() {
        // Given
        Long userId = 1L;
        Optional<ShoppingCart> optionalShoppingCart = Optional.of(new ShoppingCart());
        when(shoppingCartRepository.findByUser_Id(userId)).thenReturn(optionalShoppingCart);
        when(shoppingCartMapper.mapToShoppingCartResponse(any())).thenReturn(new ShoppingCartResponseDto());

        // When
        ShoppingCartResponseDto result = shoppingCartService.getUserShoppingCart(userId);

        // Then
        Assertions.assertNotNull(result);
        verify(shoppingCartRepository, times(1)).findByUser_Id(userId);
        verify(shoppingCartMapper, times(1)).mapToShoppingCartResponse(any());
    }

    @Test
    @DisplayName("Should add book to cart")
    void addBookToCart() {
        // Given
        Long userId = 1L;
        Long bookId = 1L;
        int quantity = 2;
        Optional<ShoppingCart> optionalShoppingCart = Optional.of(new ShoppingCart());
        Optional<Book> optionalBook = Optional.of(new Book());
        when(shoppingCartRepository.findByUser_Id(userId)).thenReturn(optionalShoppingCart);
        when(bookRepository.findById(bookId)).thenReturn(optionalBook);
        when(shoppingCartMapper.mapToShoppingCartResponse(any())).thenReturn(new ShoppingCartResponseDto());

        // When
        ShoppingCartResponseDto result = shoppingCartService.addBookToCart(userId, bookId, quantity);

        // Then
        Assertions.assertNotNull(result);
        verify(shoppingCartRepository, times(1)).findByUser_Id(userId);
        verify(bookRepository, times(1)).findById(bookId);
        verify(shoppingCartMapper, times(1)).mapToShoppingCartResponse(any());
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should update cart item quantity")
    void updateCartItemQuantity() {
        // Given
        Long cartItemId = 1L;
        int quantity = 3;
        Optional<CartItem> optionalCartItem = Optional.of(new CartItem());
        when(cartItemRepository.findById(cartItemId)).thenReturn(optionalCartItem);
        when(shoppingCartMapper.mapToCartItemResponse(any())).thenReturn(new CartItemResponseDto());

        // When
        CartItemResponseDto result = shoppingCartService.updateCartItemQuantity(cartItemId, quantity);

        // Then
        Assertions.assertNotNull(result);
        verify(cartItemRepository, times(1)).findById(cartItemId);
        verify(cartItemRepository, times(1)).save(any());
        verify(shoppingCartMapper, times(1)).mapToCartItemResponse(any());
    }

    @Test
    @DisplayName("Should remove book from cart")
    void removeBookFromCart() {
        // Given
        Long cartItemId = 1L;

        // When
        shoppingCartService.removeBookFromCart(cartItemId);

        // Then
        verify(cartItemRepository, times(1)).deleteById(cartItemId);
    }
}
