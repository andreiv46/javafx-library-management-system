package org.ardeu.librarymanagementsystem.domain.exceptions.loan;

/**
 * Exception thrown when an invalid return date is provided for a loan.
 * This exception is typically used when the return date is before the loan date or otherwise invalid.
 */
public class InvalidReturnDateException extends Exception {

    /**
     * Constructs a new {@link InvalidReturnDateException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public InvalidReturnDateException(String message) {
        super(message);
    }
}
