package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;
import org.ardeu.librarymanagementsystem.services.base.Service;

public class LoanService extends Service<Loan> {

    public LoanService(FileHandler<Loan> fileHandler) {
        super(fileHandler);
    }
}
