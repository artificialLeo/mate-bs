package com.book.store.service;

import com.book.store.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List findAll();
}
