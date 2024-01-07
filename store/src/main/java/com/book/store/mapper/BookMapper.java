package com.book.store.mapper;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.model.Book;
import com.book.store.model.Category;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(source = "categories", target = "categoryIds")
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toEntity(BookRequestDto bookRequestDto);

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

    default List<Long> map(Set<Category> categories) {
        return categories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(BookRequestDto updateBookDto, @MappingTarget Book existingBook);
}
