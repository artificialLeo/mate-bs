package com.book.store.repo;

import com.book.store.model.Book;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTests {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("""
            Get By Id
            """ )
    public void findById_idNotPresent_returnNothing() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("book");
        book.setPrice(BigDecimal.valueOf(49.95));
        bookRepository.save(book);

        Book result = bookRepository.findById(1L).orElseThrow();

        Assertions.assertEquals(1, book.getId());
    }
}
