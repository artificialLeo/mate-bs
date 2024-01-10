package com.book.store.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @DisplayName("getUserShoppingCart -> Get User Shopping Cart -> Returns ShoppingCartResponseDto")
    void getUserShoppingCart_GetUserShoppingCart_ReturnsShoppingCartResponseDto() {
        Long userId = 1L;
        Optional<ShoppingCart> optionalShoppingCart = Optional.of(new ShoppingCart());
        when(shoppingCartRepository.findByUser_Id(userId)).thenReturn(optionalShoppingCart);
        when(shoppingCartMapper.mapToShoppingCartResponse(any())).thenReturn(new ShoppingCartResponseDto());

        ShoppingCartResponseDto result = shoppingCartService.getUserShoppingCart(userId);

        Assertions.assertNotNull(result);
        verify(shoppingCartRepository, times(1)).findByUser_Id(userId);
        verify(shoppingCartMapper, times(1)).mapToShoppingCartResponse(any());
    }

    @Test
    @DisplayName("addBookToCart -> Add Book to Cart -> Returns ShoppingCartResponseDto")
    void addBookToCart_AddBookToCart_ReturnsShoppingCartResponseDto() {
        Long userId = 1L;
        Long bookId = 1L;
        int quantity = 2;
        Optional<ShoppingCart> optionalShoppingCart = Optional.of(new ShoppingCart());
        Optional<Book> optionalBook = Optional.of(new Book());
        when(shoppingCartRepository.findByUser_Id(userId)).thenReturn(optionalShoppingCart);
        when(bookRepository.findById(bookId)).thenReturn(optionalBook);
        when(shoppingCartMapper.mapToShoppingCartResponse(any())).thenReturn(new ShoppingCartResponseDto());

        ShoppingCartResponseDto result = shoppingCartService.addBookToCart(userId, bookId, quantity);

        Assertions.assertNotNull(result);
        verify(shoppingCartRepository, times(1)).findByUser_Id(userId);
        verify(bookRepository, times(1)).findById(bookId);
        verify(shoppingCartMapper, times(1)).mapToShoppingCartResponse(any());
        verify(shoppingCartRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("updateCartItemQuantity -> Update Cart Item Quantity -> Returns CartItemResponseDto")
    void updateCartItemQuantity_UpdateCartItemQuantity_ReturnsCartItemResponseDto() {
        Long cartItemId = 1L;
        int quantity = 3;
        Optional<CartItem> optionalCartItem = Optional.of(new CartItem());
        when(cartItemRepository.findById(cartItemId)).thenReturn(optionalCartItem);
        when(shoppingCartMapper.mapToCartItemResponse(any())).thenReturn(new CartItemResponseDto());

        CartItemResponseDto result = shoppingCartService.updateCartItemQuantity(cartItemId, quantity);

        Assertions.assertNotNull(result);
        verify(cartItemRepository, times(1)).findById(cartItemId);
        verify(cartItemRepository, times(1)).save(any());
        verify(shoppingCartMapper, times(1)).mapToCartItemResponse(any());
    }

    @Test
    @DisplayName("removeBookFromCart -> Remove Book from Cart -> No Return Value")
    void removeBookFromCart_RemoveBookFromCart_NoReturnValue() {
        Long cartItemId = 1L;

        shoppingCartService.removeBookFromCart(cartItemId);

        verify(cartItemRepository, times(1)).deleteById(cartItemId);
    }
}

