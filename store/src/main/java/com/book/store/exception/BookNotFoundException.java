package com.book.store.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
}

