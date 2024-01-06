package com.book.store.service.impl;

import com.book.store.dto.OrderDto;
import com.book.store.mapper.OrderMapper;
import com.book.store.model.Order;
import com.book.store.model.Status;
import com.book.store.repo.OrderRepository;
import com.book.store.service.OrderService;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderDto placeOrder(OrderDto orderDto) {
        Order order = orderMapper.mapToOrderEntity(orderDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.mapToOrderDto(savedOrder);
    }

    @Override
    public List<OrderDto> getUserOrderHistory() {
        List<Order> userOrders = orderRepository.findAll();
        return userOrders
                .stream()
                .map(orderMapper::mapToOrderDto)
                .toList();
    }

    @Override
    public void updateOrderStatus(Long orderId, Status newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: "
                        + orderId));

        order.setStatus(newStatus);
        orderRepository.save(order);
    }
}

