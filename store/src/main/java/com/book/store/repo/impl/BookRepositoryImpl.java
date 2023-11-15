package com.book.store.repo.impl;

import com.book.store.model.Book;
import com.book.store.repo.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
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
        try (Session session = sessionFactory.getCurrentSession()) {
            return session.createQuery("FROM Book", Book.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new RuntimeException("Error finding all books", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Session session = sessionFactory.getCurrentSession()) {
            Book book = session.get(Book.class, id);
            return Optional.ofNullable(book);
        } catch (PersistenceException e) {
            throw new RuntimeException("Error finding book by id: " + id, e);
        }
    }

}
