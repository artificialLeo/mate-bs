package com.book.store.service;

import com.book.store.dto.OrderDto;
import com.book.store.model.Status;
import java.util.List;

public interface OrderService {
    OrderDto placeOrder(OrderDto orderDto);

    List<OrderDto> getUserOrderHistory();

    void updateOrderStatus(Long orderId, Status newStatus);
}
