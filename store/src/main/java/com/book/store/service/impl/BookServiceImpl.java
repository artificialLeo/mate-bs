package com.book.store.service.impl;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.exception.EntityNotFoundException;
import com.book.store.mapper.BookMapper;
import com.book.store.model.Book;
import com.book.store.repo.BookRepository;
import com.book.store.service.BookService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository
                .findAll()
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found."));
    }

    @Override
    public BookDto createBook(BookRequestDto bookDto) {
        Book book = bookMapper.toBook(bookDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(Long id, BookRequestDto updateBookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Book with id " + id + " not found."));

        bookMapper.toBook(updateBookDto);
        Book updatedBook = bookRepository.save(existingBook);

        return bookMapper.toBookDto(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Book with id " + id + " not found.");
        }
    }

    @Override
    public List<BookDto> searchBooks(
            String title,
            String author,
            BigDecimal price,
            String description
    ) {
        Specification<Book> bookSpecification
                = new BookSpecifications(title, author, price, description);

        return bookRepository
                .findAll(bookSpecification)
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
