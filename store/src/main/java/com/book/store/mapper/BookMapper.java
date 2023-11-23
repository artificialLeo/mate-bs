package com.book.store.mapper;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toBook(BookRequestDto bookRequestDto);
}
