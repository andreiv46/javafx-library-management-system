package org.ardeu.librarymanagementsystem.entities.loan;

import org.ardeu.librarymanagementsystem.entities.base.BaseEntity;

import java.time.LocalDate;
import java.util.UUID;

public class Loan extends BaseEntity {
    private UUID memberId;
    private UUID bookId;
    private LocalDate loanDate;
    private LocalDate dueDate;

    public Loan(UUID id, UUID memberId, UUID bookId, LocalDate loanDate, LocalDate dueDate) {
        super(id);
        this.memberId = memberId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
    }

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", bookId=" + bookId +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                '}';
    }
}
