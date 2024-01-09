package com.book.store.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.book.store.dto.BookDto;
import com.book.store.model.Book;
import com.book.store.model.Category;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CategoryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("getBookById -> WhenBookSaved -> ReturnsCorrectDto")
    @Transactional
    @Rollback
    public void getBookById_WhenBookSaved_ReturnsCorrectDto() throws Exception {
        Book bookById = createTestBookTemplateWithCategory();

        String responseContent = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/books/{id}", bookById.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookDto bookDto = objectMapper.readValue(responseContent, BookDto.class);

        assertEquals(bookById.getId(), bookDto.getId());
        assertEquals("Existing Book", bookDto.getTitle());
        assertEquals("Existing Author", bookDto.getAuthor());
        assertEquals("1234567890002", bookDto.getIsbn());
        assertEquals(BigDecimal.valueOf(29.99), bookDto.getPrice());
        assertEquals("Existing Description", bookDto.getDescription());
        assertEquals("existing_cover.jpg", bookDto.getCoverImage());

        List<Long> categoryIds = bookDto.getCategoryIds();
        assertNotNull(categoryIds);
        assertTrue(categoryIds.contains(bookById.getCategories().stream().findFirst().orElseThrow().getId()));
    }

    @Test
    @DisplayName("getAllBooks -> WhenBooksExist -> ReturnsPageOfBooks")
    @Transactional
    @Rollback
    public void getAllBooks_WhenBooksExist_ReturnsPageOfBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    @DisplayName("createBook -> WhenValidInput -> BookCreated")
    @Transactional
    @Rollback
    public void createBook_WhenValidInput_BookCreated() throws Exception {
        BookDto bookDto = createTestBookDtoTemplate();

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
    @DisplayName("updateBook -> WhenValidInput -> BookUpdated")
    @Transactional
    @Rollback
    public void updateBook_WhenValidInput_BookUpdated() throws Exception {
        Book existingBook = createTestBookTemplateWithCategory();
        BookDto updatedBookDto = createTestBookDtoTemplate();

        String requestContent = objectMapper.writeValueAsString(updatedBookDto);
        String responseContent = mockMvc.perform(put("/api/books/{id}", existingBook.getId())
                        .content(requestContent)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        BookDto updatedBook = objectMapper.readValue(responseContent, BookDto.class);

        assertNotNull(updatedBook);
        assertEquals(existingBook.getId(), updatedBook.getId());
        assertEquals("New Book", updatedBook.getTitle());
        assertEquals(BigDecimal.valueOf(19.99), updatedBook.getPrice());
    }

    @Test
    @DisplayName("deleteBook -> WhenBookExists -> BookDeleted")
    @Transactional
    @Rollback
    public void deleteBook_WhenBookExists_BookDeleted() throws Exception {
        Book bookToDelete = createTestBookTemplateWithCategory();

        mockMvc.perform(delete("/api/books/{id}", bookToDelete.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertTrue(bookRepository.findById(bookToDelete.getId()).isPresent());
        assertTrue(bookRepository.findById(bookToDelete.getId()).orElseThrow().isDeleted());
    }

    @Test
    @DisplayName("searchBooks -> WhenSearchCriteriaExist -> ReturnsMatchingBooks")
    @Transactional
    @Rollback
    public void searchBooks_WhenSearchCriteriaExist_ReturnsMatchingBooks() throws Exception {
        createTestBookTemplateWithCategory();

        String responseContent = mockMvc.perform(get("/api/books/search")
                        .param("title", "Existing Book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<BookDto> searchResult = objectMapper.readValue(responseContent, new TypeReference<>() {});

        assertNotNull(searchResult);
        assertEquals(1, searchResult.size());
        assertEquals("Existing Book", searchResult.get(0).getTitle());
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setName("Test Category");
        category.setDescription("Category Description");
        return categoryRepository.save(category);
    }

    private Book createTestBookTemplateWithCategory() {
        Category category = createTestCategory();

        Book book = new Book();
        book.setTitle("Existing Book");
        book.setAuthor("Existing Author");
        book.setIsbn("1234567890002");
        book.setPrice(BigDecimal.valueOf(29.99));
        book.setDescription("Existing Description");
        book.setCoverImage("existing_cover.jpg");
        book.getCategories().add(category);
        return bookRepository.save(book);
    }

    private BookDto createTestBookDtoTemplate() {
        Category category = createTestCategory();

        BookDto bookDto = new BookDto();
        bookDto.setTitle("New Book");
        bookDto.setAuthor("New Author");
        bookDto.setIsbn("1234567890005");
        bookDto.setPrice(BigDecimal.valueOf(19.99));
        bookDto.setDescription("New Description");
        bookDto.setCoverImage("new_cover.jpg");
        bookDto.setCategoryIds(List.of(category.getId()));
        return bookDto;
    }

}
