package com.book.store.service;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.mapper.BookMapper;
import com.book.store.model.Book;
import com.book.store.model.Category;
import com.book.store.repo.BookRepository;
import com.book.store.service.impl.BookServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should save a book")
    void saveBook() {
        Book book = new Book();
        when(bookRepository.save(any())).thenReturn(book);

        Book result = bookService.save(new Book());

        Assertions.assertNotNull(result);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get all books")
    void getAllBooks() {
        Pageable pageable = Pageable.unpaged();
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll(pageable)).thenReturn(Page.empty());
        when(bookMapper.toDto(any())).thenReturn(new BookDto());

        List<BookDto> result = bookService.getAllBooks(pageable);

        Assertions.assertNotNull(result);
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Should get a book by ID")
    void getBookById() {
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        when(bookMapper.toDto(any())).thenReturn(new BookDto());

        BookDto result = bookService.getBookById(bookId);

        Assertions.assertNotNull(result);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).toDto(any());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when getting a non-existing book by ID")
    void getNonExistingBookById() {
        Long nonExistingBookId = 999L;
        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(nonExistingBookId));
    }

    @Test
    @DisplayName("Should create a new book")
    void createBook() {
        BookRequestDto bookDto = new BookRequestDto();
        Book book = new Book();
        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(new BookDto());

        BookDto result = bookService.createBook(bookDto);

        Assertions.assertNotNull(result);
        verify(bookMapper, times(1)).toEntity(bookDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
    }

    @Test
    @DisplayName("Should update an existing book")
    void updateBook() {
        Long bookId = 1L;
        BookRequestDto updateBookDto = new BookRequestDto();
        updateBookDto.setTitle("Updated Title");
        updateBookDto.setAuthor("Updated Author");
        updateBookDto.setIsbn("Updated ISBN");
        updateBookDto.setPrice(BigDecimal.valueOf(29.99));
        updateBookDto.setDescription("Updated Description");
        updateBookDto.setCoverImage("Updated Cover Image");

        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Existing Book Title");
        existingBook.setAuthor("Existing Book Author");
        existingBook.setIsbn("1234567890123");
        existingBook.setPrice(BigDecimal.valueOf(19.99));
        existingBook.setDescription("Existing Book Description");
        existingBook.setCoverImage("existing-book-cover.jpg");
        existingBook.setDeleted(false);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

        when(bookRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        when(bookMapper.toDto(any())).thenAnswer(invocation -> {
            Book bookArgument = invocation.getArgument(0);
            BookDto bookDto = new BookDto();
            bookDto.setId(bookArgument.getId());
            bookDto.setTitle(bookArgument.getTitle());
            bookDto.setAuthor(bookArgument.getAuthor());

            return bookDto;
        });

        BookDto result = bookService.updateBook(bookId, updateBookDto);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any());
        verify(bookMapper, times(1)).toDto(any());

        Assertions.assertNotNull(result);
        Assertions.assertEquals(updateBookDto.getTitle(), result.getTitle());
        Assertions.assertEquals(updateBookDto.getAuthor(), result.getAuthor());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when updating a non-existing book")
    void updateNonExistingBook() {
        Long nonExistingBookId = 999L;
        BookRequestDto updateBookDto = new BookRequestDto();
        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> bookService.updateBook(nonExistingBookId, updateBookDto));
    }

    @Test
    @DisplayName("Should delete an existing book")
    void deleteBook() {
        Long bookId = 1L;

        BookRepository bookRepository = mock(BookRepository.class);
        when(bookRepository.findById(bookId))
                .thenReturn(Optional.of(new Book()));
        BookService bookService = new BookServiceImpl(bookRepository, bookMapper);
        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).saveAndFlush(any());
    }




    @Test
    @DisplayName("Should search for books")
    void searchBooks() {
        BigDecimal price = BigDecimal.valueOf(29.99);
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll(any(Specification.class))).thenReturn(books);
        when(bookMapper.toDto(any())).thenReturn(new BookDto());

        List<BookDto> result = bookService.searchBooks("Title", "Author", price, "Description");

        Assertions.assertNotNull(result);
        verify(bookRepository, times(1)).findAll(any(Specification.class));
        verify(bookMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Should find books by category ID")
    void findAllByCategoryId() {
        Long categoryId = 1L;
        Pageable pageable = Pageable.unpaged();
        Page<Book> booksPage = Page.empty();
        when(bookRepository.findAllByCategoryIdAndNotDeleted(categoryId, pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(any())).thenReturn(new BookDto());

        Page<BookDto> result = bookService.findAllByCategoryId(categoryId, pageable);

        Assertions.assertNotNull(result);
        verify(bookRepository, times(1)).findAllByCategoryIdAndNotDeleted(categoryId, pageable);
        verify(bookMapper, times(0)).toDto(any());
    }
}
