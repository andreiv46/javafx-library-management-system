package org.ardeu.librarymanagementsystem.entities.loan.mappers;

import org.ardeu.librarymanagementsystem.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.entities.loan.Loan;

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
                LocalDate.parse(values[3]),
                LocalDate.parse(values[4])
        );
    }
}
