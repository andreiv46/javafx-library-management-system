package org.ardeu.librarymanagementsystem.domain.exceptions.author;

public class AuthorAlreadyExistsException extends Exception {
    public AuthorAlreadyExistsException(String message) {
        super(message);
    }
}
