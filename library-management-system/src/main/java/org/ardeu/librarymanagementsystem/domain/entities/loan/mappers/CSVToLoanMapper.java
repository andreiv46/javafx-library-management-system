package org.ardeu.librarymanagementsystem.domain.entities.loan.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanStatus;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

public class CSVToLoanMapper implements Function<String, Loan> {
    @Override
    public Loan apply(String s) {
        String[] values = s.split(",");
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
