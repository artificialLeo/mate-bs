package com.book.store.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BookRequestDto {
    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters.")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters.")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Size(min = 1, max = 13, message = "ISBN must be between 1 and 13 characters.")
    private String isbn;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    @Size(max = 2000, message = "Description can't be longer than 2000 characters.")
    private String description;

    @NotBlank(message = "Cover image URL is required")
    private String coverImage;
}
