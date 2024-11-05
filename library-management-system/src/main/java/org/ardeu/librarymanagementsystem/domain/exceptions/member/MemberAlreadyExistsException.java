package org.ardeu.librarymanagementsystem.domain.exceptions.member;

public class MemberAlreadyExistsException extends Exception {
    public MemberAlreadyExistsException(String message) {
        super(message);
    }
}
