package com.book.store.repo.impl;

import com.book.store.model.Book;
import com.book.store.repo.BookRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface BookRepositoryImpl extends JpaRepository<Book, Long>, BookRepository {

}
