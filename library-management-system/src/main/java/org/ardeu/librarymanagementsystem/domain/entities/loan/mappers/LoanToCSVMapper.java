package org.ardeu.librarymanagementsystem.domain.entities.loan.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;

import java.util.function.Function;

public class LoanToCSVMapper implements Function<Loan, String> {
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
