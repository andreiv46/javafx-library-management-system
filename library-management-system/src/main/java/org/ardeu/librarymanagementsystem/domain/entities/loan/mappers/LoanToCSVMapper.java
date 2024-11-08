package org.ardeu.librarymanagementsystem.domain.entities.loan.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;

import java.util.function.Function;

/**
 * Maps a Loan object to its CSV string representation.
 * @see Loan
 * @see Function
 */
public class LoanToCSVMapper implements Function<Loan, String> {

    /**
     * Converts a Loan object into a CSV string representation.
     * <p>
     * The CSV string will have the following structure:
     * <pre>
     * ID,Member ID,Book ID,Price,Loan Date,Due Date,Return Date (optional),Status
     * </pre>
     *
     * @param loan the Loan object to be mapped to a CSV string
     * @return a CSV string representing the Loan object
     */
    @Override
    public String apply(Loan loan) {
        return String.join(",",
                loan.getId().toString(),
                loan.getMemberId().toString(),
                loan.getBookId().toString(),
                String.valueOf(loan.getPrice()),
                loan.getLoanDate().toString(),
                loan.getDueDate().toString(),
                loan.getReturnDate() == null ? "" : loan.getReturnDate().toString(),
                loan.getStatus().toString()
        );
    }
}
