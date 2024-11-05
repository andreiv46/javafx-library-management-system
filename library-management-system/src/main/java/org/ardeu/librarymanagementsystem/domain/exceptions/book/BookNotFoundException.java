package org.ardeu.librarymanagementsystem.domain.exceptions.book;

public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }
}
