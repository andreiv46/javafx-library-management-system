package org.ardeu.librarymanagementsystem.domain.exceptions.book;

/**
 * Exception thrown when an attempt is made to add a book that already exists in the system.
 * This is a custom exception used to indicate a duplicate book in the inventory.
 */
public class BookAlreadyExistsException extends Exception {

    /**
     * Constructs a new {@link BookAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public BookAlreadyExistsException(String message) {
        super(message);
    }
}