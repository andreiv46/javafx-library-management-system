package org.ardeu.librarymanagementsystem.domain.exceptions.genre;

public class GenreAlreadyExistsException extends Exception {
    public GenreAlreadyExistsException(String message) {
        super(message);
    }
}
