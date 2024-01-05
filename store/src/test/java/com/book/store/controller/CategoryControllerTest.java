package com.book.store.controller;

import com.book.store.dto.CategoryDto;
import com.book.store.mapper.BookMapper;
import com.book.store.mapper.CategoryMapper;
import com.book.store.model.Book;
import com.book.store.model.Category;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CategoryRepository;
import com.book.store.service.BookService;
import com.book.store.service.CategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookService bookService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @InjectMocks
    private CategoryController categoryController;

    private Category category;
    private Book book;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        categoryRepository.deleteAll();
        bookRepository.deleteAll();

        // Set up common instances for tests
        category = new Category();
        category.setName("Fiction");
        category.setDescription("Fictional Books");

        categoryRepository.save(category);

        book = new Book();
        book.setTitle("The Book Title");
        book.setAuthor("John Doe");
        book.setIsbn("0000000000123");
        book.setPrice(new BigDecimal("29.99"));
        book.setDescription("A fascinating book description.");
        book.setCoverImage("book_cover.jpg");
        book.setDeleted(false);
        book.getCategories().add(category);

    }

    @Test
    void testCreateCategory_SuccessfulCreation() throws Exception {
        // Arrange
        CategoryDto categoryDto = new CategoryDto("Fiction", "Fictional Books");
        when(categoryService.save(any())).thenReturn(categoryDto);

        // Act
        ResultActions result = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)));

        // Assert
        result.andExpect(status().isCreated());
        CategoryDto createdCategory = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), CategoryDto.class);
        assertEquals("Fiction", createdCategory.getName());
        assertEquals("Fictional Books", createdCategory.getDescription());
    }

    @Test
    void testGetAllCategories_Successful() throws Exception {
        // Arrange
        List<CategoryDto> categoryDtoList = categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        Page<CategoryDto> categoryDtoPage = new PageImpl<>(categoryDtoList);

        when(categoryService.findAll(any(Pageable.class))).thenReturn(categoryDtoPage);

        // Act
        ResultActions result = mockMvc.perform(get("/api/categories"));

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println("Response Content: " + content);

        // Assert
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Fiction"))
                .andExpect(jsonPath("$.content[0].description").value("Fictional Books"));
    }

    @Test
    void testGetCategoryById_Successful() throws Exception {
        when(categoryService.getById(1L)).thenReturn(categoryMapper.toDto(category));

        // Act
        ResultActions result = mockMvc.perform(get("/api/categories/1"));

        // Assert
        result.andExpect(status().isOk());
        CategoryDto fetchedCategory = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), CategoryDto.class);
        assertEquals("Fiction", fetchedCategory.getName());
        assertEquals("Fictional Books", fetchedCategory.getDescription());
    }

    @Test
    void testUpdateCategory_SuccessfulUpdate() throws Exception {
        // Arrange
        CategoryDto updatedCategoryDto = new CategoryDto("Fantasy", "Fantasy Books");
        when(categoryService.update(1L, updatedCategoryDto)).thenReturn(updatedCategoryDto);

        // Act
        ResultActions result = mockMvc.perform(put("/api/categories/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategoryDto)));

        // Assert
        result.andExpect(status().isOk());
        CategoryDto updatedCategory = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), CategoryDto.class);
        assertEquals("Fantasy", updatedCategory.getName());
        assertEquals("Fantasy Books", updatedCategory.getDescription());
    }

    @Test
    void testGetBooksByCategoryId_Successful() throws Exception {
        // Arrange

        // Set up common instances for tests
        categoryRepository.save(category);

        bookRepository.save(book);

        // Act
        ResultActions result = mockMvc.perform(get("/api/categories/1/books"));

        // Assert
        result.andExpect(status().isOk());
        Map<String, Object> resultMap = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), new TypeReference<>() {});

        List<Map<String, Object>> contentList = (List<Map<String, Object>>) resultMap.get("content");
        System.out.println("Categories count: " + categoryRepository.count());
        System.out.println("Books count: " + bookRepository.findAll().stream().findFirst().orElseThrow().getId());
        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println("Response Content: " + content);


        assertEquals(book.getTitle(), contentList.get(0).get("title"));
        assertEquals(book.getAuthor(), contentList.get(0).get("author"));
        assertEquals(book.getIsbn(), contentList.get(0).get("isbn"));
    }

    @Test
    void testDeleteCategory_SuccessfulDeletion() throws Exception {
        // Act
        ResultActions result = mockMvc.perform(delete("/api/categories/1"));

        // Assert
        result.andExpect(status().isNoContent());
        assertTrue(categoryRepository.findById(1L).orElseThrow().isDeleted());

    }

}
