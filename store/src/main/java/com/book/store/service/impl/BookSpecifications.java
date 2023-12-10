package com.book.store.service.impl;

import com.book.store.model.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications implements Specification<Book> {
    private String title;
    private String author;
    private BigDecimal price;
    private String description;

    public BookSpecifications(String title, String author, BigDecimal price, String description) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
    }

    @Override
    public Predicate toPredicate(
            Root<Book> root,
            CriteriaQuery<?> query,
            CriteriaBuilder criteriaBuilder
    ) {
        List<Predicate> predicates = new ArrayList<>();

        if (title != null && !title.isEmpty()) {
            predicates.add(criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("title")), "%" + title.toLowerCase() + "%"));
        }

        if (author != null && !author.isEmpty()) {
            predicates.add(criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("author")), "%" + author.toLowerCase() + "%"));
        }

        if (price != null) {
            predicates.add(criteriaBuilder.equal(root.get("price"), price));
        }

        if (description != null && !description.isEmpty()) {
            predicates.add(criteriaBuilder
                    .like(criteriaBuilder
                            .lower(root.get("description")), "%"
                            + description.toLowerCase() + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
