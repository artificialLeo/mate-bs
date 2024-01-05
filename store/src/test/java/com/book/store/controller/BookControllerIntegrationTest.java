package com.book.store.controller;

import com.book.store.config.TestDataInitializer;
import com.book.store.dto.BookDto;
import com.book.store.model.Book;
import com.book.store.model.Category;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getBookByIdUsingMockMvc_WhenBookSaved_DtoIsCorrect() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1234566690007");
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
        assertEquals("1234566690007", bookDto.getIsbn());
        assertEquals(BigDecimal.valueOf(29.99), bookDto.getPrice());
        assertEquals("Description 1", bookDto.getDescription());
        assertEquals("cover1.jpg", bookDto.getCoverImage());

        List<Long> categoryIds = bookDto.getCategoryIds();
        assertNotNull(categoryIds);
        assertFalse(categoryIds.isEmpty());
        assertEquals(1L, categoryIds.get(0));
    }

    @Test
    public void getAllBooksUsingMockMvc_WhenBooksExist_ReturnsPageOfBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }


    @Test
    public void createBookUsingMockMvc_WhenValidInput_BookCreated() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        BookDto bookDto = new BookDto();
        bookDto.setTitle("New Book");
        bookDto.setAuthor("New Author");
        bookDto.setIsbn("1234567890005");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setDescription("New Description");
        bookDto.setCoverImage("new_cover.jpg");
        bookDto.setCategoryIds(List.of(category.getId()));

        String requestContent = objectMapper.writeValueAsString(bookDto);

        String responseContent = mockMvc.perform(post("/api/books")
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookDto createdBook = objectMapper.readValue(responseContent, BookDto.class);

        assertNotNull(createdBook);
        assertNotNull(createdBook.getId());
        assertEquals("New Book", createdBook.getTitle());
    }

    @Test
    public void updateBookUsingMockMvc_WhenValidInput_BookUpdated() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        Book existingBook = new Book();
        existingBook.setTitle("Existing Book");
        existingBook.setAuthor("Existing Author");
        existingBook.setIsbn("1234567890004");
        existingBook.setPrice(BigDecimal.valueOf(29.99));
        existingBook.setDescription("Existing Description");
        existingBook.setCoverImage("existing_cover.jpg");
        existingBook.getCategories().add(category);
        bookRepository.save(existingBook);

        BookDto updatedBookDto = new BookDto();
        updatedBookDto.setTitle("Updated Book");
        updatedBookDto.setAuthor("Updated Author");
        updatedBookDto.setIsbn("1234567890003");
        updatedBookDto.setPrice(BigDecimal.valueOf(39.99));
        updatedBookDto.setDescription("Updated Description");
        updatedBookDto.setCoverImage("updated_cover.jpg");
        updatedBookDto.setCategoryIds(List.of(category.getId()));

        String requestContent = objectMapper.writeValueAsString(updatedBookDto);

        String responseContent = mockMvc.perform(put("/api/books/{id}", existingBook.getId())
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookDto updatedBook = objectMapper.readValue(responseContent, BookDto.class);

        assertNotNull(updatedBook);
        assertEquals(existingBook.getId(), updatedBook.getId());
        assertEquals("Updated Book", updatedBook.getTitle());
        assertEquals(BigDecimal.valueOf(39.99), updatedBook.getPrice());
    }

    @Test
    public void deleteBookUsingMockMvc_WhenBookExists_BookDeleted() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        Book bookToDelete = new Book();
        bookToDelete.setTitle("Existing Book");
        bookToDelete.setAuthor("Existing Author");
        bookToDelete.setIsbn("1234567890002");
        bookToDelete.setPrice(BigDecimal.valueOf(29.99));
        bookToDelete.setDescription("Existing Description");
        bookToDelete.setCoverImage("existing_cover.jpg");
        bookToDelete.getCategories().add(category);
        bookToDelete.setDeleted(false);
        bookRepository.save(bookToDelete);

        mockMvc.perform(delete("/api/books/{id}", bookToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(bookRepository.findById(bookToDelete.getId()).isPresent());
        assertTrue(bookRepository.findById(bookToDelete.getId()).orElseThrow().isDeleted());
    }

    @Test
    public void searchBooksUsingMockMvc_WhenSearchCriteriaExist_ReturnsMatchingBooks() throws Exception {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Search Book");
        book.setAuthor("Existing Author");
        book.setIsbn("1234567890001");
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setDescription("Existing Description");
        book.setCoverImage("existing_cover.jpg");
        book.getCategories().add(category);
        bookRepository.save(book);

        String responseContent = mockMvc.perform(get("/api/books/search")
                        .param("title","Search Book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BookDto> searchResult = objectMapper.readValue(responseContent, new TypeReference<>() {});

        assertNotNull(searchResult);
        assertEquals("Search Book", searchResult.get(0).getTitle());
    }

}
