package org.ardeu.librarymanagementsystem.domain.entities.loan;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for creating a new loan.
 * This DTO contains the essential information needed to create a loan record.
 *
 * @param bookId the unique identifier of the book being loaned
 * @param memberId the unique identifier of the member who is borrowing the book
 * @param dueDate the date by which the book should be returned
 * @param price the price of the loan
 */
public record LoanCreationDTO(UUID bookId, UUID memberId, LocalDate dueDate, double price) { }
