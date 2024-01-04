package com.book.store.repo;

import com.book.store.config.CustomMySqlContainer;
import com.book.store.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@DataJpaTest
//@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest
//@Transactional
public class BookRepositoryTests {
    @Autowired
    private BookRepository bookRepository;

//    @Autowired
//    private TestDataInitializer testDataInitializer;

    private static CustomMySqlContainer customMySqlContainer;

    @BeforeEach
    public void setUp() {
        customMySqlContainer = CustomMySqlContainer.getInstance();
        customMySqlContainer.start();
//        testDataInitializer.initializeTestData();
    }

    @Test
    @DisplayName("Should find books by Category ID when category exists")
    public void findAllByCategoryId_CategoryExists_ReturnBooks() {
        Long categoryId = 1L;
        Pageable pageable = Pageable.unpaged();

        int actualSize = bookRepository.findAllByCategoryId(categoryId, pageable).stream().toList().size();
        int expectedSize = 1;

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    @DisplayName("Should find book by ID and is not deleted when book exists")
    public void findByIdAndIsDeletedFalse_BookExists_ReturnBook() {
        Book savedBook = bookRepository.findAll().get(0);
        Optional<Book> foundBook = bookRepository.findByIdAndIsDeletedFalse(savedBook.getId());

        Assertions.assertTrue(foundBook.isPresent());

        long expectedId = savedBook.getId();
        long actualId = foundBook.get().getId();
        Assertions.assertEquals(expectedId, actualId);

        long expectedSize = 5;
        long actualSize = bookRepository.findAll().size();
        Assertions.assertEquals(expectedSize, actualSize);
    }
}
