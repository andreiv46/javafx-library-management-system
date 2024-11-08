package org.ardeu.librarymanagementsystem.domain.exceptions.author;

/**
 * Custom exception thrown when an attempt is made to add or create an author
 * that already exists in the system.
 */
public class AuthorAlreadyExistsException extends Exception {

    /**
     * Constructs an {@link AuthorAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the {@link Throwable#getMessage()} method
     */
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}