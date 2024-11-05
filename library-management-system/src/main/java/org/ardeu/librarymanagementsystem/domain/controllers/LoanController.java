package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.InventoryNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.InventoryService;
import org.ardeu.librarymanagementsystem.domain.services.LoanService;
import org.ardeu.librarymanagementsystem.domain.services.MemberService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.loan.LoanDTOValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LoanController {
    private final LoanService loanService = ServiceRegistry.getInstance().getService(LoanService.class);
    private final MemberService memberService = ServiceRegistry.getInstance().getService(MemberService.class);
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);
    private final Validator<LoanDTO> loanValidator = new LoanDTOValidator();

    public Result<ObservableMap<UUID, Loan>> getAllLoans() {
        ObservableMap<UUID, Loan> loans = loanService.getItems();
        return Result.success(loans);
    }

    public Result<List<Loan>> addLoansForUser(List<LoanDTO> loanDTOS, UUID memberId) {
        List<Loan> loans = new ArrayList<>();

        try {
            // Validate loanDto
            for (LoanDTO loanDTO : loanDTOS) {
                this.loanValidator.validate(loanDTO);
            }

            // Create loans and update inventory
            for (LoanDTO loanDTO : loanDTOS) {
                Loan loan = loanService.create(loanDTO);
                loans.add(loan);
                this.inventoryService.updateInventory(loan.getBookId());
                this.memberService.addLoanToMember(memberId, loan.getId());
                this.loanService.add(loan);
            }

            return Result.success(loans);
        }
        catch (ValidationException e) {
            return Result.failure("Validation failed:\n" + e.getMessage());
        } catch (MemberNotFoundException | InventoryNotFoundException | DuplicateItemException e) {
            e.printStackTrace();
            revertAddLoans(loans);
            return Result.failure(e.getMessage());
        }
    }

    private void revertAddLoans(List<Loan> loans) {
        for (Loan loan : loans) {
            this.inventoryService.revertInventoryUpdate(loan.getBookId());
            this.loanService.remove(loan.getId());
            this.memberService.removeLoanFromMember(loan.getMemberId(), loan.getId());
        }
    }
}
