package com.book.store.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.store.dto.CartItemResponseDto;
import com.book.store.dto.ShoppingCartRequestDto;
import com.book.store.dto.ShoppingCartResponseDto;
import com.book.store.dto.UpdateQuantityRequestDto;
import com.book.store.service.ShoppingCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class ShoppingCartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("getUserShoppingCart -> Successful -> ReturnsAccepted")
    void getUserShoppingCart_ValidUserId_Successful() throws Exception {
        ShoppingCartResponseDto mockShoppingCart = new ShoppingCartResponseDto();
        when(shoppingCartService.getUserShoppingCart(anyLong())).thenReturn(mockShoppingCart);

        ResultActions result = mockMvc.perform(get("/api/cart?userId=1"));

        result.andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(mockShoppingCart)));
    }

    @Test
    @DisplayName("addBookToCart -> Successful -> ReturnsAccepted")
    void addBookToCart_ValidShoppingCartRequestDtoAndUserId_Successful() throws Exception {
        ShoppingCartResponseDto mockShoppingCart = new ShoppingCartResponseDto();
        when(shoppingCartService.addBookToCart(anyLong(), anyLong(), anyInt())).thenReturn(mockShoppingCart);

        ShoppingCartRequestDto requestDto = new ShoppingCartRequestDto();
        requestDto.setBookId(1L);
        requestDto.setQuantity(2);

        ResultActions result = mockMvc.perform(post("/api/cart?userId=1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        result.andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(mockShoppingCart)));
    }

    @Test
    @DisplayName("updateCartItemQuantity -> Successful -> ReturnsAccepted")
    void updateCartItemQuantity_ValidCartItemId_Successful() throws Exception {
        CartItemResponseDto mockCartItem = new CartItemResponseDto();
        when(shoppingCartService.updateCartItemQuantity(anyLong(), anyInt())).thenReturn(mockCartItem);

        UpdateQuantityRequestDto requestDto = new UpdateQuantityRequestDto();
        requestDto.setQuantity(3);

        ResultActions result = mockMvc.perform(put("/api/cart/cart-items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)));

        result.andExpect(status().isAccepted())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(mockCartItem)));
    }

    @Test
    @DisplayName("removeBookFromCart -> Successful -> ReturnsAccepted")
    void removeBookFromCart_ValidCartItemId_Successful() throws Exception {
        doNothing().when(shoppingCartService).removeBookFromCart(anyLong());

        ResultActions result = mockMvc.perform(delete("/api/cart/cart-items/1"));

        result.andExpect(status().isAccepted());
    }
}
