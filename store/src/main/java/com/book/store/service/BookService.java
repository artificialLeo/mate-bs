package com.book.store.service;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<BookDto> getAllBooks();

    BookDto getBookById(Long id);

    BookDto createBook(BookRequestDto bookDto);

    BookDto updateBook(Long id, BookRequestDto updateBookDto);

    void deleteBook(Long id);
}

