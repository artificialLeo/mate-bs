package com.book.store.service;

import com.book.store.dto.OrderDto;
import com.book.store.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDto placeOrder(OrderDto orderDto);

    Page<OrderDto> getUserOrderHistory(Pageable pageable);

    void updateOrderStatus(Long orderId, Status newStatus);
}
