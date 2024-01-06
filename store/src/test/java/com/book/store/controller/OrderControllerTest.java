package com.book.store.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.store.dto.OrderDto;
import com.book.store.dto.OrderItemDto;
import com.book.store.model.Status;
import com.book.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
    @DisplayName("placeOrder -> ValidOrder -> ReturnsCreatedStatus")
    void placeOrder_ValidOrder_ReturnsCreatedStatus() throws Exception {
        OrderDto orderDto = createValidOrderDto();
        OrderDto createdOrder = createValidOrderDto();
        when(orderService.placeOrder(any(OrderDto.class))).thenReturn(createdOrder);

        ResultActions result = mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(createdOrder.getId()))
                .andExpect(jsonPath("$.userId").value(createdOrder.getUserId()));
    }

    @Test
    @DisplayName("getUserOrderHistory -> ReturnsOrderHistory")
    void getUserOrderHistory_ReturnsOrderHistory() throws Exception {
        List<OrderDto> orderHistory = Arrays.asList(createValidOrderDto(), createValidOrderDto());
        when(orderService.getUserOrderHistory()).thenReturn(orderHistory);

        ResultActions result = mockMvc.perform(get("/api/orders"));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(orderHistory.get(0).getId()))
                .andExpect(jsonPath("$[1].id").value(orderHistory.get(1).getId()));
    }

    @Test
    @DisplayName("updateOrderStatus -> ValidOrderId -> ReturnsNoContent")
    void updateOrderStatus_ValidOrderId_ReturnsNoContent() throws Exception {
        Long orderId = 1L;
        OrderDto orderDto = createValidOrderDto();

        ResultActions result = mockMvc.perform(patch("/api/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDto)));

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
