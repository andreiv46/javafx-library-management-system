package org.ardeu.librarymanagementsystem.domain.exceptions.inventory;

/**
 * Exception thrown when there are no available copies of a book for borrowing.
 * This exception is typically used when attempting to borrow a book that has no remaining available copies in the inventory.
 */
public class NoAvailableCopiesException extends Exception {

    /**
     * Constructs a new {@link NoAvailableCopiesException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public NoAvailableCopiesException(String message) {
        super(message);
    }
}

