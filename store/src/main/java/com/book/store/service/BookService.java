package com.book.store.service;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.model.Book;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<BookDto> getAllBooks(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto createBook(BookRequestDto bookDto);

    BookDto updateBook(Long id, BookRequestDto updateBookDto);

    void deleteBook(Long id);

    List<BookDto> searchBooks(String title, String author, BigDecimal price, String description);
}
