package org.ardeu.librarymanagementsystem.domain.exceptions.validation;

import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

/**
 * Exception thrown when an object fails validation.
 * This exception is typically used by {@link Validator} implementations when an object does not meet the required validation criteria.
 */
public class ValidationException extends Exception {

    /**
     * Constructs a new ValidationException with the specified detail message.
     *
     * @param message the detail message explaining the cause of the exception
     */
    public ValidationException(String message) {
        super(message);
    }
}
