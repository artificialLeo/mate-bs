package com.book.store.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum RoleName {
    @Enumerated(EnumType.STRING)
    ROLE_USER,

    @Enumerated(EnumType.STRING)
    ROLE_ADMIN
}
