package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanStatus;
import org.ardeu.librarymanagementsystem.domain.exceptions.loan.InvalidReturnDateException;
import org.ardeu.librarymanagementsystem.domain.exceptions.loan.LoanNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing {@link Loan} entities, including creating, removing, updating,
 * and retrieving loan information, as well as calculating revenue from loans.
 */
public class LoanService extends Service<Loan> {

    /**
     * Constructs a new {@link LoanService} with the specified file handler.
     *
     * @param fileHandler the file handler to be used for saving and loading {@link Loan} data
     */
    public LoanService(MapFileHandler<UUID, Loan> fileHandler) {
        super(fileHandler);
    }

    /**
     * Creates a new {@link Loan} instance based on the provided {@link LoanCreationDTO}.
     *
     * @param loanDTO the data transfer object containing loan information
     * @return the newly created {@link Loan}
     */
    public Loan create(LoanCreationDTO loanDTO) {
        return new Loan(
                UUID.randomUUID(),
                loanDTO.memberId(),
                loanDTO.bookId(),
                loanDTO.price(),
                LocalDate.now(),
                loanDTO.dueDate(),
                null,
                LoanStatus.ACTIVE
        );
    }

    /**
     * Removes a {@link Loan} from the system.
     *
     * @param id the ID of the loan to remove
     */
    public void remove(UUID id) {
        if (Objects.nonNull(id)) {
            super.items.remove(id);
        }
    }

    /**
     * Retrieves a {@link Loan} by its ID.
     *
     * @param id the ID of the loan to retrieve
     * @return the {@link Loan} object associated with the provided ID
     * @throws LoanNotFoundException if no loan is found for the specified ID
     */
    public Loan getById(UUID id) throws LoanNotFoundException {
        Loan loan = super.getItems().getOrDefault(id, null);
        if (Objects.isNull(loan)) {
            throw new LoanNotFoundException("Loan not found for id: " + id);
        }
        return loan;
    }

    /**
     * Marks a loan as returned by setting the return date and changing the loan status.
     *
     * @param loan the {@link Loan} to mark as returned
     * @param value the return date for the loan
     * @throws InvalidReturnDateException if the return date is before the loan date
     */
    public void markLoanAsReturned(Loan loan, LocalDate value) throws InvalidReturnDateException {
        if (value.isBefore(loan.getLoanDate())) {
            throw new InvalidReturnDateException("Return date for loan with id: " +
                    loan.getId() + " cannot be before loan date");
        }
        loan.setReturnDate(value);
        loan.setStatus(LoanStatus.RETURNED);
        super.getItems().remove(loan.getId());
        super.getItems().put(loan.getId(), loan);
    }

    /**
     * Marks a loan as borrowed by resetting the return date and changing the loan status.
     *
     * @param loan the {@link Loan} to mark as borrowed
     */
    public void markLoanAsBorrowed(Loan loan) {
        loan.setReturnDate(null);
        loan.setStatus(LoanStatus.ACTIVE);
        super.getItems().remove(loan.getId());
        super.getItems().put(loan.getId(), loan);
    }

    /**
     * Calculates the total revenue from loans made in the last 30 days.
     *
     * @return the total revenue from loans in the last 30 days
     */
    public Double getRevenueForLast30Days() {
        return super.getItems().values().stream()
                .filter(loan -> loan.getLoanDate().isAfter(LocalDate.now().minusDays(30)))
                .mapToDouble(Loan::getPrice)
                .sum();
    }

    /**
     * Calculates the total revenue from all loans.
     *
     * @return the total revenue from all loans
     */
    public Double getAllTimeRevenue() {
        return super.getItems().values().stream()
                .mapToDouble(Loan::getPrice)
                .sum();
    }

    /**
     * Calculates the total revenue from loans made in the last year.
     *
     * @return the total revenue from loans in the last year
     */
    public Double getRevenueForPastYear() {
        return super.getItems().values().stream()
                .filter(loan -> loan.getLoanDate().isAfter(LocalDate.now().minusYears(1)))
                .mapToDouble(Loan::getPrice)
                .sum();
    }

    /**
     * Retrieves a list of loans made in a specific year.
     *
     * @param year the year to filter loans by
     * @return a list of loans made in the specified year
     */
    public List<Loan> getLoansFrom(int year) {
        return super.getItems().values().stream()
                .filter(loan -> loan.getLoanDate().getYear() == year)
                .toList();
    }

    /**
     * Calculates the total revenue per year.
     *
     * @return a map where the key is the year and the value is the total revenue for that year
     */
    public Map<Integer, Double> getRevenuePerYear() {
        return super.getItems()
                .values()
                .stream()
                .collect(Collectors.toMap(
                        loan -> loan.getLoanDate().getYear(),
                        Loan::getPrice,
                        Double::sum
                ));
    }

    /**
     * Retrieves the total number of loans made for a specific book.
     *
     * @param id the ID of the book
     * @return the total number of loans for the specified book
     */
    public long getTotalLoansByBookId(UUID id) {
        return super.getItems()
                .values()
                .stream()
                .filter(loan -> loan.getBookId().equals(id))
                .count();
    }

    /**
     * Retrieves the total number of loans made for a set of books.
     *
     * @param bookIds a set of book IDs
     * @return the total number of loans for the specified books
     */
    public long getTotalLoansByBookId(Set<UUID> bookIds) {
        return bookIds.stream()
                .map(this::getTotalLoansByBookId)
                .mapToLong(Long::longValue)
                .sum();
    }
}

