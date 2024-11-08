package org.ardeu.librarymanagementsystem.domain.exceptions.member;

/**
 * Exception thrown when a member is not found in the system.
 * This exception is typically used when attempting to retrieve or access
 * a member that does not exist in the system.
 */
public class MemberNotFoundException extends Exception {

    /**
     * Constructs a new {@link MemberNotFoundException} with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()} method)
     */
    public MemberNotFoundException(String message) {
        super(message);
    }
}