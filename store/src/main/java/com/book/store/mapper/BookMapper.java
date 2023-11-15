package com.book.store.mapper;

import com.book.store.dto.BookDto;
import com.book.store.dto.CreateBookRequestDto;
import com.book.store.model.Book;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper
public interface BookMapper {
    BookDto toBookDto(Book book);

    List<BookDto> toBookDtoList(List<Book> books);

    Book toBook(CreateBookRequestDto createBookRequestDto);
}
