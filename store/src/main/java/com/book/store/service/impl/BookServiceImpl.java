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
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public List<BookDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }


    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Book with id " + id + " not found."));
    }

    @Override
    public BookDto createBook(BookRequestDto bookDto) {
        Book book = bookMapper.toEntity(bookDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookDto updateBook(Long id, BookRequestDto updateBookDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(()
                        -> new EntityNotFoundException("Book with id " + id + " not found."));

        bookMapper.toEntity(updateBookDto);
        Book updatedBook = bookRepository.save(existingBook);

        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        bookOptional.ifPresent(book -> {
            book.setDeleted(true);
            bookRepository.save(book);
        });
    }

    @Override
    public Page<BookDto> searchBooks(
            String title,
            String author,
            BigDecimal price,
            String description,
            Pageable pageable) {
        Specification<Book> bookSpecification = new BookSpecifications(title, author, price, description);

        Page<Book> booksPage = bookRepository.findAll(bookSpecification, pageable);
        return booksPage.map(bookMapper::toDto);
    }

    @Override
    public Page<BookDto> findAllByCategoryId(Long categoryId, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAllByCategoryId(categoryId, pageable);
        return booksPage.map(bookMapper::toDto);
    }

}
