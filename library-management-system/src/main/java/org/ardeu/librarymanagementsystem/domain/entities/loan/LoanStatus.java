package org.ardeu.librarymanagementsystem.domain.entities.loan;

/**
 * Enum representing the possible statuses of a loan.
 * A loan can be either active or returned.
 */
public enum LoanStatus {

    /**
     * The loan is currently active, and the book has not been returned.
     */
    ACTIVE,

    /**
     * The loan has been completed, and the book has been returned.
     */
    RETURNED;
}