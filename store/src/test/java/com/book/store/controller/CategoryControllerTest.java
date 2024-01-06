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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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

        category = new Category();
        category.setName("Fiction");
        category.setDescription("Fictional Books");


        book = new Book();
        book.setTitle("The Book Title");
        book.setAuthor("John Doe");
        book.setIsbn(generateRandomIsbn());
        book.setPrice(new BigDecimal("29.99"));
        book.setDescription("A fascinating book description.");
        book.setCoverImage("book_cover.jpg");
        book.setDeleted(false);
        book.getCategories().add(category);

        categoryRepository.saveAndFlush(category);
        bookRepository.saveAndFlush(book);

    }

    public static String generateRandomIsbn() {
        // Create a list of characters from the ISBN string
        List<Character> isbnChars = new ArrayList<>();
        for (char digit : "0123456789123".toCharArray()) {
            isbnChars.add(digit);
        }

        // Shuffle the characters
        Collections.shuffle(isbnChars);

        // Create a StringBuilder to reconstruct the shuffled ISBN
        StringBuilder shuffledIsbn = new StringBuilder();
        for (char digit : isbnChars) {
            shuffledIsbn.append(digit);
        }

        return shuffledIsbn.toString();
    }

    @Test
    @Transactional
    void testCreateCategory_SuccessfulCreation() throws Exception {
        CategoryDto categoryDto = new CategoryDto("Fiction", "Fictional Books");
        when(categoryService.save(any())).thenReturn(categoryDto);

        ResultActions result = mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)));

        result.andExpect(status().isCreated());
        CategoryDto createdCategory = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), CategoryDto.class);
        assertEquals("Fiction", createdCategory.getName());
        assertEquals("Fictional Books", createdCategory.getDescription());
    }

    @Test
    @Transactional
    void testGetAllCategories_Successful() throws Exception {
        List<CategoryDto> categoryDtoList = categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();

        Page<CategoryDto> categoryDtoPage = new PageImpl<>(categoryDtoList);

        when(categoryService.findAll(any(Pageable.class))).thenReturn(categoryDtoPage);

        ResultActions result = mockMvc.perform(get("/api/categories"));

        String content = result.andReturn().getResponse().getContentAsString();
        System.out.println("Response Content: " + content);

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Fiction"))
                .andExpect(jsonPath("$.content[0].description").value("Fictional Books"));
    }

    @Test
    @Transactional
    void testGetCategoryById_Successful() throws Exception {
        when(categoryService.getById(1L)).thenReturn(categoryMapper.toDto(category));

        assertEquals(categoryRepository.findAll().get(0).getId(), 4);
        ResultActions result = mockMvc.perform(get("/api/categories/4"));

        result.andExpect(status().isOk());
        CategoryDto fetchedCategory = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), CategoryDto.class);
        assertEquals("Fiction", fetchedCategory.getName());
        assertEquals("Fictional Books", fetchedCategory.getDescription());
    }

    @Test
    @Transactional
    void testUpdateCategory_SuccessfulUpdate() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto("Fantasy", "Fantasy Books");
        when(categoryService.update(1L, updatedCategoryDto)).thenReturn(updatedCategoryDto);

        ResultActions result = mockMvc.perform(put("/api/categories/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCategoryDto)));

        result.andExpect(status().isOk());
        CategoryDto updatedCategory = objectMapper.readValue(result.andReturn().getResponse().getContentAsString(), CategoryDto.class);
        assertEquals("Fantasy", updatedCategory.getName());
        assertEquals("Fantasy Books", updatedCategory.getDescription());
    }

    @Test
    @Transactional
    void testGetBooksByCategoryId_Successful() throws Exception {
//        categoryRepository.save(category);
//        bookRepository.save(book);

        assertEquals(bookRepository.findAll().get(0).getId(), 3);
        assertEquals(categoryRepository.findAll().get(0).getId(), 3);
        ResultActions result = mockMvc.perform(get("/api/categories/3/books"));



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
    @Transactional
    void testDeleteCategory_SuccessfulDeletion() throws Exception {
//        categoryRepository.save(category);
        assertEquals(categoryRepository.findAll().get(0).getId(), 5);
        ResultActions result = mockMvc.perform(delete("/api/categories/5"));

        result.andExpect(status().isNoContent());
        assertTrue(categoryRepository.findById(5L).orElseThrow().isDeleted());
    }

}
