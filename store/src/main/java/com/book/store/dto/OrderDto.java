package com.book.store.dto;

import com.book.store.model.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderDto {
    @NotNull
    private Long id;

    @NotNull
    private Long userId;

    @Valid
    @NotEmpty
    private Set<OrderItemDto> orderItems;

    @NotNull
    private LocalDateTime orderDate;

    @NotNull
    private BigDecimal total;

    @NotNull
    private Status status;

    @NotBlank
    private String shippingAddress;
}
