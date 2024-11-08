package org.ardeu.librarymanagementsystem.domain.exceptions.genre;

/**
 * Exception thrown when an attempt is made to add a genre that already exists.
 * This exception is typically used to signal that a genre with the same name or identifier
 * already exists in the system and cannot be added again.
 */
public class GenreAlreadyExistsException extends Exception {

    /**
     * Constructs a new {@link GenreAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public GenreAlreadyExistsException(String message) {
        super(message);
    }
}