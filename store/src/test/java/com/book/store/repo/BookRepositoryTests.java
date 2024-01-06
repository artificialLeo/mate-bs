package com.book.store.repo;

import com.book.store.config.CustomMySqlContainer;
import com.book.store.config.TestDataInitializer;
import com.book.store.model.Book;
import com.book.store.model.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class BookRepositoryTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setUp() {
        testDataInitializer.initializeTestData();
    }

    @Test
    @DisplayName("Should find books by Category ID when category exists")
    public void findAllByCategoryId_CategoryExists_ReturnBooks() {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1234562390123");
        book1.setPrice(BigDecimal.valueOf(29.99));
        book1.setDescription("Description 1");
        book1.setCoverImage("cover1.jpg");
        book1.getCategories().add(category);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("9843543210987");
        book2.setPrice(BigDecimal.valueOf(39.99));
        book2.setDescription("Description 2");
        book2.setCoverImage("cover2.jpg");
        book2.getCategories().add(category);
        bookRepository.save(book2);

        Long categoryId = category.getId();
        Pageable pageable = Pageable.unpaged();

        List<Book> books = bookRepository.findAllByCategoryIdAndNotDeleted(categoryId, pageable).stream().toList();

        int expectedSize = 2;
        int actualSize = books.size();
        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    @DisplayName("Should find book by ID and is not deleted when book exists")
    public void findByIdAndIsDeletedFalse_BookExists_ReturnBook() {
        Book savedBook = bookRepository.findAll().get(0);
        Optional<Book> foundBook = bookRepository.findByIdAndDeletedFalse(savedBook.getId());

        Assertions.assertTrue(foundBook.isPresent());

        long expectedId = savedBook.getId();
        long actualId = foundBook.get().getId();
        Assertions.assertEquals(expectedId, actualId);

        long expectedSize = 5;
        long actualSize = bookRepository.findAll().size();
        Assertions.assertEquals(expectedSize, actualSize);
    }
}
