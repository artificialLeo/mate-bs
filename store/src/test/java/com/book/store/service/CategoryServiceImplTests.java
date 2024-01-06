package com.book.store.service;

import com.book.store.dto.CategoryDto;
import com.book.store.mapper.CategoryMapper;
import com.book.store.model.Category;
import com.book.store.repo.BookRepository;
import com.book.store.repo.CategoryRepository;
import com.book.store.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTests {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private BookRepository bookRepository;


    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should find all categories")
    void findAllCategories() {
        Pageable pageable = Pageable.unpaged();
        Page<Category> categoryPage = Page.empty();
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(any())).thenReturn(new CategoryDto());

        Page<CategoryDto> result = categoryService.findAll(pageable);

        Assertions.assertNotNull(result);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Should get a category by ID")
    void getCategoryById() {
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(new Category()));
        when(categoryMapper.toDto(any())).thenReturn(new CategoryDto());

        CategoryDto result = categoryService.getById(categoryId);

        Assertions.assertNotNull(result);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).toDto(any());
    }

    @Test
    @DisplayName("Should throw RuntimeException when getting a non-existing category by ID")
    void getNonExistingCategoryById() {
        Long nonExistingCategoryId = 999L;
        when(categoryRepository.findById(nonExistingCategoryId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> categoryService.getById(nonExistingCategoryId));
    }

    @Test
    @DisplayName("Should save a category")
    void saveCategory() {
        CategoryDto categoryDto = new CategoryDto();
        Category category = new Category();
        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(new CategoryDto());

        CategoryDto result = categoryService.save(categoryDto);

        Assertions.assertNotNull(result);
        verify(categoryMapper, times(1)).toEntity(categoryDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
    }

    @Test
    @DisplayName("Should update an existing category")
    void updateCategory() {
        Long categoryId = 1L;
        CategoryDto updateCategoryDto = new CategoryDto();
        Category existingCategory = new Category();
        Category updatedCategory = new Category();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        doNothing().when(categoryMapper).updateCategoryFromDto(updateCategoryDto, existingCategory);
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(new CategoryDto());

        CategoryDto result = categoryService.update(categoryId, updateCategoryDto);

        Assertions.assertNotNull(result);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryMapper, times(1)).updateCategoryFromDto(updateCategoryDto, existingCategory);
        verify(categoryRepository, times(1)).save(existingCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
    }

    @Test
    @DisplayName("Should throw RuntimeException when updating a non-existing category")
    void updateNonExistingCategory() {
        Long nonExistingCategoryId = 999L;
        CategoryDto updateCategoryDto = new CategoryDto();
        when(categoryRepository.findById(nonExistingCategoryId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> categoryService.update(nonExistingCategoryId, updateCategoryDto));
    }

    @Test
    @DisplayName("Should delete an existing category by ID")
    void deleteCategoryById() {
        Long categoryId = 1L;

        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(new Category()));
        CategoryService categoryService = new CategoryServiceImpl(categoryRepository, bookRepository, categoryMapper);
        categoryService.deleteById(categoryId);

        verify(categoryRepository, times(1)).saveAndFlush(any());
    }

}
