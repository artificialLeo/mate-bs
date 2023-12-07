package com.book.store.repo;

import com.book.store.model.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndDeletedFalse(Long id);
}
