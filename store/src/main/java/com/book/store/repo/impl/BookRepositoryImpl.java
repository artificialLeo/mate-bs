package com.book.store.repo.impl;

import com.book.store.model.Book;
import com.book.store.repo.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private EntityManager entityManager;

    private SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(book);
            return book;
        } catch (PersistenceException e) {
            throw new RuntimeException("Error saving book", e);
        }
    }

    @Override
    public List<Book> findAll() {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery("FROM Book", Book.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new RuntimeException("Error finding all books", e);
        }
    }
}
