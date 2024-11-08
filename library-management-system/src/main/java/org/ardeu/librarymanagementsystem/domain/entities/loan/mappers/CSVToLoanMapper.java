package org.ardeu.librarymanagementsystem.domain.entities.loan.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanStatus;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

/**
 * Maps a CSV string representation of a loan to a Loan object.
 * @see Loan
 * @see Function
 */
public class CSVToLoanMapper implements Function<String, Loan> {

    /**
     * Converts a CSV string into a Loan object.
     * <p>
     * The CSV string must have the following structure:
     * <pre>
     * ID,Member ID,Book ID,Price,Loan Date,Due Date,Return Date (optional),Status
     * </pre>
     *
     * @param s the CSV string to be mapped to a Loan object
     * @return a Loan object corresponding to the provided CSV string
     * @throws IllegalArgumentException if the CSV string is in an invalid format or contains invalid data
     */
    @Override
    public Loan apply(String s) {
        String[] values = s.split(",");
        if (values.length != 8) {
            throw new IllegalArgumentException("Invalid CSV string: " + s);
        }
        return new Loan(
                UUID.fromString(values[0]),
                UUID.fromString(values[1]),
                UUID.fromString(values[2]),
                Double.parseDouble(values[3]),
                LocalDate.parse(values[4]),
                LocalDate.parse(values[5]),
                values[6].isEmpty() ? null : LocalDate.parse(values[6]),
                LoanStatus.valueOf(values[7])
        );
    }
}
