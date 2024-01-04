package com.book.store.controller;

import com.book.store.config.TestDataInitializer;
import com.book.store.dto.BookDto;
import com.book.store.model.Book;
import com.book.store.model.Category;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CategoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TestDataInitializer testDataInitializer;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
//        testDataInitializer.initializeTestData();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getBookByIdUsingMockMvc() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1234566690123");
        book1.setPrice(BigDecimal.valueOf(29.99));
        book1.setDescription("Description 1");
        book1.setCoverImage("cover1.jpg");
        book1.getCategories().add(category);
        bookRepository.save(book1);

        String responseContent = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookDto bookDto = objectMapper.readValue(responseContent, BookDto.class);

        assertEquals(1L, bookDto.getId());
        assertEquals("Book 1", bookDto.getTitle());
        assertEquals("Author 1", bookDto.getAuthor());
        assertEquals("1234562390123", bookDto.getIsbn());
        assertEquals(BigDecimal.valueOf(29.99), bookDto.getPrice());
        assertEquals("Description 1", bookDto.getDescription());
        assertEquals("cover1.jpg", bookDto.getCoverImage());

        List<Long> categoryIds = bookDto.getCategoryIds();
        assertNotNull(categoryIds);
        assertFalse(categoryIds.isEmpty());
        assertEquals(1L, categoryIds.get(0));
    }
}
