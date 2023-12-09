package com.book.store.service;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.model.Book;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Book save(Book book);

    List<BookDto> getAllBooks(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto createBook(BookRequestDto bookDto);

    BookDto updateBook(Long id, BookRequestDto updateBookDto);

    void deleteBook(Long id);

    List<BookDto> searchBooks(
            String title,
            String author,
            BigDecimal price,
            String description
    );

    Page<BookDto> findAllByCategoryId(Long categoryId, Pageable pageable);

}
