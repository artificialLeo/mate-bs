package com.book.store.service.impl;

import com.book.store.dto.OrderItemDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.OrderItemMapper;
import com.book.store.repo.OrderItemRepository;
import com.book.store.service.OrderItemService;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Set<OrderItemDto> getAllOrderItems(Long orderId) {
        return orderItemRepository
                .findAllByOrderId(orderId)
                .stream()
                .map(orderItemMapper::mapToOrderItemDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemDto getOrderItem(Long orderId, Long itemId) {
        return orderItemRepository
                .findByIdAndOrderId(itemId, orderId)
                .map(orderItemMapper::mapToOrderItemDto)
                .orElseThrow(()
                        -> new EntityNotFoundException("OrderItem not found with id: " + itemId));
    }

}
