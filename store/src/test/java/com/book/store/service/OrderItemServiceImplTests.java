package com.book.store.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.store.dto.OrderItemDto;
import com.book.store.mapper.OrderItemMapper;
import com.book.store.repo.OrderItemRepository;
import com.book.store.service.impl.OrderItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    @DisplayName("getAllOrderItems -> Get All Order Items for a Given Order ID -> Returns Set of OrderItemDto")
    void getAllOrderItems_GetAll_ReturnsSetOfOrderItemDto() {
        Long orderId = 1L;
        when(orderItemRepository.findAllByOrderId(orderId)).thenReturn(Collections.emptySet());

        Set<OrderItemDto> result = orderItemService.getAllOrderItems(orderId);

        Assertions.assertNotNull(result);
        verify(orderItemRepository, times(1)).findAllByOrderId(orderId);
        verify(orderItemMapper, times(0)).mapToOrderItemDto(any());
    }

    @Test
    @DisplayName("getOrderItem -> Get Order Item for a Given Order ID and Item ID -> Throws EntityNotFoundException")
    void getOrderItem_GetOrderItem_ThrowsEntityNotFoundException() {
        Long orderId = 1L;
        Long itemId = 1L;
        when(orderItemRepository.findByIdAndOrderId(itemId, orderId)).thenReturn(Optional.ofNullable(null));

        Assertions.assertThrows(EntityNotFoundException.class, () -> orderItemService.getOrderItem(orderId, itemId));
        verify(orderItemRepository, times(1)).findByIdAndOrderId(itemId, orderId);
        verify(orderItemMapper, times(0)).mapToOrderItemDto(any());
    }
}
