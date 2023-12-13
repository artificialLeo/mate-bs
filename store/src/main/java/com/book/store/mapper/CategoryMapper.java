package com.book.store.mapper;

import com.book.store.dto.CategoryDto;
import com.book.store.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto createCategoryDto);

    void updateCategoryFromDto(
            CategoryDto updateCategoryDto,
            @MappingTarget Category category
    );
}
