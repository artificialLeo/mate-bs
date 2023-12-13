package com.book.store.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateQuantityRequestDto {
    @Min(value = 0, message = "Quantity must be greater than or equal to 0")
    @NotNull
    private int quantity;
}
