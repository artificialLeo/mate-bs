package com.book.store.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.store.dto.OrderDto;
import com.book.store.mapper.OrderMapper;
import com.book.store.model.Order;
import com.book.store.model.Status;
import com.book.store.repo.OrderRepository;
import com.book.store.service.impl.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrderServiceImplTests {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("placeOrder -> Place Order -> Returns OrderDto")
    void placeOrder_PlaceOrder_ReturnsOrderDto() {
        OrderDto orderDto = new OrderDto();
        Order order = new Order();
        when(orderMapper.mapToOrderEntity(orderDto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.mapToOrderDto(order)).thenReturn(new OrderDto());

        OrderDto result = orderService.placeOrder(orderDto);

        Assertions.assertNotNull(result);
        verify(orderMapper, times(1)).mapToOrderEntity(orderDto);
        verify(orderRepository, times(1)).save(order);
        verify(orderMapper, times(1)).mapToOrderDto(order);
    }

    @Test
    @DisplayName("getUserOrderHistory -> Get User Order History -> Returns List of OrderDto")
    void getUserOrderHistory_GetUserOrderHistory_ReturnsListOfOrderDto() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderDto> result = orderService.getUserOrderHistory();

        Assertions.assertNotNull(result);
        verify(orderRepository, times(1)).findAll();
        verify(orderMapper, times(0)).mapToOrderDto(any());
    }

    @Test
    @DisplayName("updateOrderStatus -> Update Order Status -> No Return Value")
    void updateOrderStatus_UpdateOrderStatus_NoReturnValue() {
        Long orderId = 1L;
        Status newStatus = Status.PENDING;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        orderService.updateOrderStatus(orderId, newStatus);

        verify(orderRepository, times(1)).findById(orderId);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("updateOrderStatus -> Throws EntityNotFoundException for Non-Existing Order")
    void updateOrderStatus_NonExistingOrder_ThrowsEntityNotFoundException() {
        Long nonExistingOrderId = 999L;
        Status newStatus = Status.PENDING;
        when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> orderService.updateOrderStatus(nonExistingOrderId, newStatus));

        verify(orderRepository, times(1)).findById(nonExistingOrderId);
        verify(orderRepository, times(0)).save(any());
    }
}
