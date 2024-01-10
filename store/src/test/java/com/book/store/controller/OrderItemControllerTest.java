package com.book.store.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.store.dto.OrderItemDto;
import com.book.store.service.OrderItemService;
import java.math.BigDecimal;
import java.util.HashSet;
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
class OrderItemControllerTest {
    @MockBean
    private OrderItemService orderItemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("getAllOrderItems -> Successful -> ReturnsOk")
    void getAllOrderItems_ExistingOrderItemId_Successful() throws Exception {
        Set<OrderItemDto> orderItems = createOrderItems();
        when(orderItemService.getAllOrderItems(anyLong())).thenReturn(orderItems);

        ResultActions result = mockMvc.perform(get("/api/orders/1/items")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(orderItems.iterator().next().getId()));
    }

    @Test
    @DisplayName("getOrderItem -> Successful -> ReturnsOk")
    void getOrderItem_ExistingOrderItemIdAndItemId_Successful() throws Exception {
        OrderItemDto orderItem = createOrderItem();
        when(orderItemService.getOrderItem(anyLong(), anyLong())).thenReturn(orderItem);

        ResultActions result = mockMvc.perform(get("/api/orders/1/items/2")
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderItem.getId()));
    }

    private Set<OrderItemDto> createOrderItems() {
        Set<OrderItemDto> orderItems = new HashSet<>();
        OrderItemDto orderItem = createOrderItem();
        orderItems.add(orderItem);
        return orderItems;
    }

    private OrderItemDto createOrderItem() {
        OrderItemDto orderItem = new OrderItemDto();
        orderItem.setId(1L);
        orderItem.setBookId(2L);
        orderItem.setQuantity(3);
        orderItem.setPrice(new BigDecimal("25.99"));
        return orderItem;
    }
}

