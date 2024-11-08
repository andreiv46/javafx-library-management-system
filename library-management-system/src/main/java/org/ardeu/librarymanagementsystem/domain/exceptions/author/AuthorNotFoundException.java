package org.ardeu.librarymanagementsystem.domain.exceptions.author;

/**
 * Custom exception thrown when an author is not found in the system.
 */
public class AuthorNotFoundException extends Exception {

    /**
     * Constructs an {@link AuthorNotFoundException} with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the {@link Throwable#getMessage()} method
     */
    public AuthorNotFoundException(String message) {
        super(message);
    }
}
