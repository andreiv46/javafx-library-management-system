package org.ardeu.librarymanagementsystem.domain.exceptions.entity;

/**
 * Exception thrown when an attempt is made to add an item that already exists.
 * This is typically used when trying to add an item with a duplicate ID
 * to a collection that does not allow duplicates.
 */
public class DuplicateItemException extends Exception {

    /**
     * Constructs a new {@code DuplicateItemException} with the specified detail message.
     *
     * @param message the detail message, which is saved for later retrieval by the {@link #getMessage()} method
     */
    public DuplicateItemException(String message) {
        super(message);
    }
}
