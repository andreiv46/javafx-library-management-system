package org.ardeu.librarymanagementsystem.domain.exceptions.book;

/**
 * Exception thrown when a book is not found in the system.
 * This custom exception is used to indicate that the requested book does not exist in the inventory.
 */
public class BookNotFoundException extends Exception {

    /**
     * Constructs a new {@link BookNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public BookNotFoundException(String message) {
        super(message);
    }
}
