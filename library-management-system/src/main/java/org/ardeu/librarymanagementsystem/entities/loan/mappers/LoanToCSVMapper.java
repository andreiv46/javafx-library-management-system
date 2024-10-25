package org.ardeu.librarymanagementsystem.entities.loan.mappers;

import org.ardeu.librarymanagementsystem.entities.loan.Loan;

import java.util.function.Function;

public class LoanToCSVMapper implements Function<Loan, String> {
    @Override
    public String apply(Loan loan) {
        return String.join(",",
                loan.getId().toString(),
                loan.getMemberId().toString(),
                loan.getBookId().toString(),
                loan.getLoanDate().toString(),
                loan.getDueDate().toString()
        );
    }
}
