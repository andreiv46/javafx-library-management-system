package org.ardeu.librarymanagementsystem.domain.exceptions.member;

/**
 * Exception thrown when a member already exists in the system.
 * This exception is typically used when attempting to add or register a member
 * who already exists based on a unique identifier such as email.
 */
public class MemberAlreadyExistsException extends Exception {

    /**
     * Constructs a new {@link MemberAlreadyExistsException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}