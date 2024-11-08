package org.ardeu.librarymanagementsystem.domain.exceptions.loan;

/**
 * Exception thrown when a loan is not found in the system.
 * This exception is typically used when attempting to retrieve or operate on a loan that does not exist.
 */
public class LoanNotFoundException extends Exception {

    /**
     * Constructs a new {@link LoanNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public LoanNotFoundException(String message) {
        super(message);
    }
}
