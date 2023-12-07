package com.book.store.dto;

import java.util.List;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDto {
    private Long id;

    private String title;

    private String author;

    private String isbn;

    private BigDecimal price;

    private String description;

    private String coverImage;

    private List<Long> categoryIds;
}
