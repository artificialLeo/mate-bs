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
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1234562390123");
        book1.setPrice(BigDecimal.valueOf(29.99));
        book1.setDescription("Description 1");
        book1.setCoverImage("cover1.jpg");
        book1.getCategories().add(category);
        bookRepository.save(book1);

        book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("9843543210987");
        book2.setPrice(BigDecimal.valueOf(39.99));
        book2.setDescription("Description 2");
        book2.setCoverImage("cover2.jpg");
        book2.getCategories().add(category);
        bookRepository.save(book2);
    }

    @Test
    @DisplayName("findAllByCategoryId -> List size")
    @Transactional
    @Rollback
    public void findAllByCategoryId_CategoryExists_ReturnBooks() {
        Long categoryId = category.getId();
        Pageable pageable = Pageable.unpaged();

        List<Book> books = bookRepository.findAllByCategoryIdAndNotDeleted(categoryId, pageable).stream().toList();

        int expected = 2;
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

        long expected = 2;
        long actual = bookRepository.findAll().size();
        Assertions.assertEquals(expected, actual);
    }
}
