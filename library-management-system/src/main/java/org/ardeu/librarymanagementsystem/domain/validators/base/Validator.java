package org.ardeu.librarymanagementsystem.domain.validators.base;

import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;

public interface Validator<T> {
    void validate(T t) throws ValidationException;
}
