package com.book.store.controller;

import com.book.store.dto.BookDto;
import com.book.store.mapper.BookMapper;
import com.book.store.service.BookService;
import org.json.JSONException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookService bookService;

//    @Test
//    @DisplayName("Given available books in the database, retrieve the next available book")
//    public void getNextAvailableBook_BooksAvailable_ReturnsExpectedBook() {
//        // Mocking the BookService response
//        when(bookService.getAllBooks(any())).thenReturn(books);
//
//        // when
//        String actual = restTemplate.getForObject("/api/books/next", String.class);
//
//        // then
//        assertEquals("Book Title 1", actual);
//    }

    @Test
    @DisplayName("Retrieve all books from API and compare with expected JSON")
    public void getAllBooksFromApi_ReturnsExpectedJson() throws JSONException {
        // Mocking the BookService response
        BookDto book1 = new BookDto(
                1L, "Book Title 1", "Author 1", "ISBN-123456", BigDecimal.valueOf(19.99),
                "Description 1", "cover1.jpg", Arrays.asList(1L, 2L));

        BookDto book2 = new BookDto(
                2L, "Book Title 2", "Author 2", "ISBN-654321", BigDecimal.valueOf(29.99),
                "Description 2", "cover2.jpg", Arrays.asList(3L, 4L, 5L));

        List<BookDto> books = Arrays.asList(book1, book2);

        bookService.save(BookMapper.);
        bookService.save(book2);

        // Mocking the Pageable
        Pageable pageable = PageRequest.of(0, 10);

        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(books);

        // when
        String actualJson = restTemplate.getForObject("/api/books", String.class);

        // then
        String expectedJson = "[{\"id\":1,\"title\":\"Book Title 1\",\"author\":\"Author 1\"," +
                "\"isbn\":\"ISBN-123456\",\"price\":19.99,\"description\":\"Description 1\"," +
                "\"cover_image\":\"cover1.jpg\",\"category_ids\":[1,2]}," +
                "{\"id\":2,\"title\":\"Book Title 2\",\"author\":\"Author 2\"," +
                "\"isbn\":\"ISBN-654321\",\"price\":29.99,\"description\":\"Description 2\"," +
                "\"cover_image\":\"cover2.jpg\",\"category_ids\":[3,4,5]}]";

        JSONAssert.assertEquals(expectedJson, actualJson, false);
    }

}