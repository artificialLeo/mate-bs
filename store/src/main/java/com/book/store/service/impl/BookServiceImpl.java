package com.book.store.service.impl;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.mapper.BookMapper;
import com.book.store.model.Book;
import com.book.store.model.Category;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CategoryRepository;
import com.book.store.service.BookService;
import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found."));

        existingBook.setTitle(updateBookDto.getTitle());
        existingBook.setAuthor(updateBookDto.getAuthor());
        existingBook.setIsbn(updateBookDto.getIsbn());
        existingBook.setPrice(updateBookDto.getPrice());
        existingBook.setDescription(updateBookDto.getDescription());
        existingBook.setCoverImage(updateBookDto.getCoverImage());


        Book updatedBook = bookRepository.save(existingBook);

//        BookDto bookDto = new BookDto();
//        bookDto.setId(updatedBook.getId());
//        bookDto.setAuthor(updatedBook.getAuthor());
//        bookDto.setCategoryIds(updatedBook.getCategories()
//                .stream()
//                .map(Category::getId)
//                .toList());
//        bookDto.setDescription(updatedBook.getDescription());
//        bookDto.setPrice(updatedBook.getPrice());
//        bookDto.setIsbn(updatedBook.getIsbn());
//        bookDto.setTitle(updatedBook.getTitle());
//        bookDto.setCoverImage(updatedBook.getCoverImage());
//
//        return bookDto;

        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Book with id " + id + " not found."));
        bookToDelete.setDeleted(true);
        bookRepository.saveAndFlush(bookToDelete);
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
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public Page<BookDto> findAllByCategoryId(Long categoryId, Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAllByCategoryIdAndNotDeleted(categoryId, pageable);
        return booksPage.map(bookMapper::toDto);
    }
}
