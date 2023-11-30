package com.book.store.controller;

import com.book.store.dto.BookDto;
import com.book.store.dto.BookRequestDto;
import com.book.store.service.BookService;
import java.math.BigDecimal;
import java.util.List;

import com.book.store.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @Operation(summary = "Get all books with pagination and sorting")
    public ResponseEntity<Page<BookDto>> getAll(
            @Parameter(description = "Page number (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sorting parameter") @RequestParam(required = false) String sort) {
        Pageable pageable = Utils.createPageable(page, size, sort);
        List<BookDto> books = bookService.getAllBooks(pageable);

        return ResponseEntity.ok(new PageImpl<>(books, pageable, books.size()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a book by ID")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        BookDto book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping
    @Operation(summary = "Create a new book")
    public ResponseEntity<BookDto> createBook(@Validated @RequestBody BookRequestDto bookDto) {
        BookDto createdBook = bookService.createBook(bookDto);
        return ResponseEntity.ok(createdBook);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a book by ID")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id, @RequestBody BookRequestDto bookDto) {
        BookDto updatedBook = bookService.updateBook(id, bookDto);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book by ID")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search books based on criteria")
    public ResponseEntity<List<BookDto>> searchBooks(
            @Parameter(description = "Title of the book") @RequestParam(required = false) String title,
            @Parameter(description = "Author of the book") @RequestParam(required = false) String author,
            @Parameter(description = "Price of the book") @RequestParam(required = false) BigDecimal price,
            @Parameter(description = "Description of the book") @RequestParam(required = false) String description) {

        List<BookDto> result = bookService.searchBooks(title, author, price, description);
        return ResponseEntity.ok(result);
    }
}
