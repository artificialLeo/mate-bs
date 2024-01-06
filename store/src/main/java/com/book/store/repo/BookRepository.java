package com.book.store.repo;

import com.book.store.model.Book;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    @Query("SELECT b FROM Book b JOIN b.categories c WHERE c.id = :categoryId AND b.deleted = false")
    Page<Book> findAllByCategoryIdAndNotDeleted(@Param("categoryId") Long categoryId, Pageable pageable);

    Optional<Book> findByIdAndDeletedFalse(Long id);
}

