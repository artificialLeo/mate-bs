package com.book.store.service;

import com.book.store.dto.OrderItemDto;
import com.book.store.mapper.OrderItemMapper;
import com.book.store.repo.OrderItemRepository;
import com.book.store.service.impl.OrderItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OrderItemServiceImplTests {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should get all order items for a given order ID")
    void getAllOrderItems() {
        Long orderId = 1L;
        when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(Collections.emptySet());

        Set<OrderItemDto> result = orderItemService.getAllOrderItems(orderId);

        Assertions.assertNotNull(result);
        verify(orderItemRepository, times(1)).findAllByOrderId(orderId);
        verify(orderItemMapper, times(0)).mapToOrderItemDto(any());
    }

    @Test
    @DisplayName("Should get a specific order item for a given order ID and item ID")
    void getOrderItem() {
        Long orderId = 1L;
        Long itemId = 1L;
        when(orderItemRepository.findByIdAndOrderId(itemId, orderId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(EntityNotFoundException.class, () -> orderItemService.getOrderItem(orderId, itemId));
        verify(orderItemRepository, times(1)).findByIdAndOrderId(itemId, orderId);
        verify(orderItemMapper, times(0)).mapToOrderItemDto(any());
    }
}
