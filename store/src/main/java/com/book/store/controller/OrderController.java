package com.book.store.controller;

import com.book.store.dto.OrderDto;
import com.book.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place an order")
    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto createdOrder = orderService.placeOrder(orderDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdOrder);
    }

    @Operation(summary = "Retrieve user's order history")
    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrderHistory() {
        List<OrderDto> orderHistory = orderService
                .getUserOrderHistory();
        return ResponseEntity.ok(orderHistory);
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderDto orderDto
    ) {
        orderService.updateOrderStatus(id, orderDto.getStatus());
        return ResponseEntity.noContent().build();
    }
}
