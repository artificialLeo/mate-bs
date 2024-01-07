package com.book.store.repo;

import com.book.store.model.Book;
import com.book.store.model.Category;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
public class BookRepositoryTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        Book newBook = new Book();
        newBook.setTitle("Book 1");
        newBook.setAuthor("Author 1");
        newBook.setIsbn("1234562390123");
        newBook.setPrice(BigDecimal.valueOf(29.99));
        newBook.setDescription("Description 1");
        newBook.setCoverImage("cover1.jpg");
        newBook.getCategories().add(category);
        bookRepository.save(newBook);
    }

    @Test
    @DisplayName("findAllByCategoryId -> List size")
    @Transactional
    @Rollback
    public void findAllByCategoryId_CategoryExists_ReturnBooks() {
        Long categoryId = category.getId();
        Pageable pageable = Pageable.unpaged();

        List<Book> books = bookRepository.findAllByCategoryIdAndNotDeleted(categoryId, pageable).stream().toList();

        int expected = 1;
        int actual = books.size();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByIdAndIsDeletedFalse -> Book Id")
    @Transactional
    @Rollback
    public void findByIdAndIsDeletedFalse_BookExists_ReturnBook() {
        Book savedBook = bookRepository.findAll().get(0);
        Optional<Book> foundBook = bookRepository.findByIdAndDeletedFalse(savedBook.getId());

        Assertions.assertTrue(foundBook.isPresent());

        long expected = savedBook.getId();
        long actual = foundBook.get().getId();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("findByIdAndIsDeletedFalse -> List size")
    @Transactional
    @Rollback
    public void findByIdAndIsDeletedFalse_BookExists_ReturnBooks() {
        Book savedBook = bookRepository.findAll().get(0);
        Optional<Book> foundBook = bookRepository.findByIdAndDeletedFalse(savedBook.getId());

        Assertions.assertTrue(foundBook.isPresent());

        long expected = 1;
        long actual = bookRepository.findAll().size();
        Assertions.assertEquals(expected, actual);
    }
}
