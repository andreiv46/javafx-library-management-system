package org.ardeu.librarymanagementsystem.domain.exceptions.inventory;

/**
 * Exception thrown when an inventory record is not found in the system.
 * This exception is typically used when attempting to access or perform an operation on an inventory
 * that does not exist for a specific book.
 */
public class InventoryNotFoundException extends Exception {

    /**
     * Constructs a new {@link InventoryNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public InventoryNotFoundException(String message) {
        super(message);
    }
}