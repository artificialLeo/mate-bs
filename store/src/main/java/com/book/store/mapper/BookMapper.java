package com.book.store.mapper;

import com.book.store.dto.BookDto;
import com.book.store.dto.CreateBookRequestDto;
import com.book.store.model.Book;
import org.mapstruct.Mapper;

@Mapper
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toBook(CreateBookRequestDto createBookRequestDto);
}
