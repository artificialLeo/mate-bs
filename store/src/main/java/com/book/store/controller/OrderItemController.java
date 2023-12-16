package com.book.store.controller;

import com.book.store.dto.OrderItemDto;
import com.book.store.service.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders/{orderId}/items")
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Operation(summary = "Retrieve all OrderItems for a specific order")
    @GetMapping
    public ResponseEntity<Set<OrderItemDto>> getAllOrderItems(@PathVariable Long orderId) {
        Set<OrderItemDto> orderItems = orderItemService.getAllOrderItems(orderId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @Operation(summary = "Retrieve a specific OrderItem within an order")
    @GetMapping("/{itemId}")
    public ResponseEntity<OrderItemDto> getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId
    ) {
        OrderItemDto orderItem = orderItemService.getOrderItem(orderId, itemId);
        return new ResponseEntity<>(orderItem, HttpStatus.OK);
    }
}
