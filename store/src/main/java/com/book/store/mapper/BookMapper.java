package com.book.store.mapper;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookDtoWithoutCategoryIds;
import com.book.store.dto.BookRequestDto;
import com.book.store.model.Book;
import com.book.store.model.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toEntity(BookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book != null && book.getCategories() != null) {
            List<Long> categoryIds = book.getCategories()
                    .stream()
                    .map(Category::getId)
                    .toList();

            bookDto.setCategoryIds(categoryIds);
        }
    }
}
