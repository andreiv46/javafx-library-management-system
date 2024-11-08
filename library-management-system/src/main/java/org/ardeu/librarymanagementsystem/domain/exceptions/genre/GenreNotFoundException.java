package org.ardeu.librarymanagementsystem.domain.exceptions.genre;

/**
 * Exception thrown when a genre is not found in the system.
 * This exception is typically used when attempting to access or perform an operation on a genre
 * that does not exist or has not been registered in the system.
 */
public class GenreNotFoundException extends Exception {

    /**
     * Constructs a new {@link GenreNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public GenreNotFoundException(String message) {
        super(message);
    }
}