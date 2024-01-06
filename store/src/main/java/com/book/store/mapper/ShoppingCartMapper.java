package com.book.store.mapper;

import com.book.store.dto.CartItemResponseDto;
import com.book.store.dto.ShoppingCartResponseDto;
import com.book.store.model.CartItem;
import com.book.store.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(source = "cartItem.book.id", target = "bookId")
    @Mapping(source = "cartItem.book.title", target = "bookTitle")
    CartItemResponseDto mapToCartItemResponse(CartItem cartItem);

    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto mapToShoppingCartResponse(ShoppingCart shoppingCart);
}
