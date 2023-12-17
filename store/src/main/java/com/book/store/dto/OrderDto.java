package com.book.store.dto;

import com.book.store.model.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "Id must be greater than 0")
    private Long id;

    @NotNull(message = "User Id cannot be null")
    @Min(value = 1, message = "User Id must be greater than 0")
    private Long userId;

    @Valid
    @NotEmpty(message = "Order items cannot be empty")
    private Set<OrderItemDto> orderItems;

    @NotNull(message = "Order date cannot be null")
    private LocalDateTime orderDate;

    @NotNull(message = "Total amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal total;

    @NotNull(message = "Order status cannot be null")
    private Status status;

    @NotBlank(message = "Shipping address cannot be blank")
    private String shippingAddress;
}
