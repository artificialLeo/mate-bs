package com.book.store.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum Status {
    @Enumerated(EnumType.STRING)
    PENDING,

    @Enumerated(EnumType.STRING)
    PROCESSING,

    @Enumerated(EnumType.STRING)
    SHIPPED,

    @Enumerated(EnumType.STRING)
    DELIVERED,

    @Enumerated(EnumType.STRING)
    CANCELED
}
