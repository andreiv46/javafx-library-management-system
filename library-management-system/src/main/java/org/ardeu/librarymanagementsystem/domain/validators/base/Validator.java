package org.ardeu.librarymanagementsystem.domain.validators.base;

import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;

/**
 * A generic interface for validating objects of type {@link T}.
 *
 * @param <T> the type of object to be validated
 */
public interface Validator<T> {

    /**
     * Validates the given object.
     *
     * @param t the object to be validated
     * @throws ValidationException if the object is not valid
     */
    void validate(T t) throws ValidationException;
}
