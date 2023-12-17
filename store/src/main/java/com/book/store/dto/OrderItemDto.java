package com.book.store.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class OrderItemDto {
    @NotNull
    private Long id;

    @NotNull
    private Long bookId;

    @Min(value = 1, message = "Quantity must be greater than or equal to 1")
    private int quantity;

    @DecimalMin(value = "0.0", message = "Price must be greater than or equal to 0.0")
    @Digits(integer = 10, fraction = 2, message = "Invalid price format")
    private BigDecimal price;
}
