package com.book.store.mapper;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MapperConfig;

@Mapper
@MapperConfig(componentModel = "spring")
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toBook(BookRequestDto bookRequestDto);
}
