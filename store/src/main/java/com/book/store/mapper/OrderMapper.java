package com.book.store.mapper;

import com.book.store.dto.OrderDto;
import com.book.store.dto.OrderItemDto;
import com.book.store.model.Order;
import com.book.store.model.OrderItem;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = { BookMapper.class, UserMapper.class })
public interface OrderMapper {

    @Mapping(source = "orderItems", target = "orderItems")
    @Mapping(source = "user.id", target = "userId")
    OrderDto mapToOrderDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemDto mapToOrderItemDto(OrderItem orderItem);

    Set<OrderItemDto> mapToOrderItemDtoSet(Set<OrderItem> orderItems);

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    Order mapToOrderEntity(OrderDto orderDto);

    Set<Order> mapToOrderEntitySet(Set<OrderDto> orderDto);
}
