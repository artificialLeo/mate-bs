package com.book.store.controller;

import com.book.store.dto.OrderDto;
import com.book.store.dto.OrderItemDto;
import com.book.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.book.store.dto.OrderDto;
import com.book.store.model.Status;
import com.book.store.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void testPlaceOrder_ValidOrder_ReturnsCreatedStatus() throws Exception {
        // Arrange
        OrderDto orderDto = createValidOrderDto();
        OrderDto createdOrder = createValidOrderDto(); // Adjust as needed

        when(orderService.placeOrder(any(OrderDto.class))).thenReturn(createdOrder);

        // Act
        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdOrder.getId()))
                .andExpect(jsonPath("$.userId").value(createdOrder.getUserId()));
    }

    @Test
    void testGetUserOrderHistory_ReturnsOrderHistory() throws Exception {
        // Arrange
        List<OrderDto> orderHistory = Arrays.asList(createValidOrderDto(), createValidOrderDto()); // Adjust as needed
        when(orderService.getUserOrderHistory()).thenReturn(orderHistory);

        // Act
        ResultActions result = mockMvc.perform(get("/api/orders"));

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderHistory.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(orderHistory.get(1).getId()));
    }

    @Test
    void testUpdateOrderStatus_ValidOrder_ReturnsNoContent() throws Exception {
        // Arrange
        Long orderId = 1L;
        OrderDto orderDto = createValidOrderDto(); // Adjust as needed

        // Act
        ResultActions result = mockMvc.perform(patch("/api/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        // Assert
        result.andExpect(status().isNoContent());
        verify(orderService).updateOrderStatus(eq(orderId), any(Status.class));
    }

    private OrderDto createValidOrderDto() {
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1L);
        orderDto.setUserId(2L);

        Set<OrderItemDto> orderItems = new HashSet<>();
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setId(1L);
        orderItemDto.setBookId(3L);
        orderItemDto.setQuantity(2);
        orderItemDto.setPrice(new BigDecimal("45.00"));
        orderItems.add(orderItemDto);

        orderDto.setOrderItems(orderItems);
        orderDto.setOrderDate(LocalDateTime.now());
        orderDto.setTotal(new BigDecimal("50.00"));
        orderDto.setStatus(Status.PENDING);
        orderDto.setShippingAddress("123 Main St, City");

        return orderDto;
    }
}

