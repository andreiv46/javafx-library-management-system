package org.ardeu.librarymanagementsystem.domain.exceptions.book;

public class BookAlreadyExistsException extends Exception {
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}
