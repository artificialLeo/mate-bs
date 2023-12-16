package com.book.store.service;

import com.book.store.dto.OrderItemDto;
import java.util.Set;

public interface OrderItemService {
    Set<OrderItemDto> getAllOrderItems(Long orderId);

    OrderItemDto getOrderItem(Long orderId, Long itemId);
}
