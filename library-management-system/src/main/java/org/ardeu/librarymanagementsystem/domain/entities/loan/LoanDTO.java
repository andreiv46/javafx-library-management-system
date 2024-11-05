package org.ardeu.librarymanagementsystem.domain.entities.loan;

import java.time.LocalDate;
import java.util.UUID;

public record LoanDTO(UUID bookId, UUID memberId, LocalDate dueDate, double price) { }
