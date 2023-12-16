package com.book.store.controller;

import com.book.store.dto.OrderDto;
import com.book.store.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    @Operation(summary = "Retrieve user's order history")
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getUserOrderHistory(
            Pageable pageable
    ) {
        Page<OrderDto> orderHistory = orderService
                .getUserOrderHistory(pageable);
        return new ResponseEntity<>(orderHistory, HttpStatus.OK);
    }

    @Operation(summary = "Update order status")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody OrderDto orderDto
    ) {
        orderService.updateOrderStatus(id, orderDto.getStatus());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

