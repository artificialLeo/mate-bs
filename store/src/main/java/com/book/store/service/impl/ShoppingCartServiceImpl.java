package com.book.store.service.impl;

import com.book.store.dto.CartItemResponseDto;
import com.book.store.dto.ShoppingCartResponseDto;
import com.book.store.mapper.ShoppingCartMapper;
import com.book.store.model.Book;
import com.book.store.model.CartItem;
import com.book.store.model.ShoppingCart;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CartItemRepository;
import com.book.store.repo.ShoppingCartRepository;
import com.book.store.service.ShoppingCartService;

import java.util.HashSet;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartResponseDto getUserShoppingCart(Long userId) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser_Id(userId);
        return shoppingCartMapper.mapToShoppingCartResponse(optionalShoppingCart.orElse(null));
    }

    @Override
    public ShoppingCartResponseDto addBookToCart(Long userId, Long bookId, int quantity) {
        Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findByUser_Id(userId);
        ShoppingCart shoppingCart = optionalShoppingCart.orElse(new ShoppingCart());

        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(quantity);

            if (shoppingCart.getCartItems() == null) {
                shoppingCart.setCartItems(new HashSet<>());
            }

            shoppingCart.getCartItems().add(cartItem);
        }

        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.mapToShoppingCartResponse(shoppingCart);
    }

    @Override
    public CartItemResponseDto updateCartItemQuantity(Long cartItemId, int quantity) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(cartItemId);
        optionalCartItem.ifPresent(cartItem -> {
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        });

        return shoppingCartMapper.mapToCartItemResponse(optionalCartItem.orElseThrow());
    }

    @Override
    public void removeBookFromCart(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
}
