package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanDTO;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanStatus;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class LoanService extends Service<Loan> {

    public LoanService(MapFileHandler<UUID, Loan> fileHandler) {
        super(fileHandler);
    }

    public Loan create(LoanDTO loanDTO) {
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

    public void remove(UUID id) {
        if(Objects.nonNull(id)) {
            super.items.remove(id);
        }
    }
}
