package com.book.store.mapper;

import com.book.store.dto.OrderItemDto;
import com.book.store.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto mapToOrderItemDto(OrderItem orderItem);
}
