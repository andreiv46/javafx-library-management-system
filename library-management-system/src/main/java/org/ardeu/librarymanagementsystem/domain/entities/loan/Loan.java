package org.ardeu.librarymanagementsystem.domain.entities.loan;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a loan transaction in the library management system.
 * A loan consists of a member borrowing a book for a specified period and the associated loan details.
 * <p>
 *     This class extends {@link BaseEntity} to inherit common entity properties.
 * </p>
 * @see BaseEntity
 * @see Book
 * @see Member
 */
public class Loan extends BaseEntity {
    /**
     * The unique identifier of the member who borrowed the book.
     */
    private UUID memberId;

    /**
     * The unique identifier of the book being loaned.
     */
    private UUID bookId;

    /**
     * The price of the loan.
     */
    private double price;

    /**
     * The date the loan was made.
     */
    private LocalDate loanDate;

    /**
     * The due date for returning the loaned book.
     */
    private LocalDate dueDate;

    /**
     * The actual date the book was returned. Null if not returned yet.
     */
    private LocalDate returnDate;

    /**
     * The current status of the loan (e.g., active, completed, overdue).
     */
    private LoanStatus status;

    /**
     * Constructs a new Loan with the specified parameters.
     *
     * @param id the unique identifier for the loan
     * @param memberId the unique identifier of the member who borrowed the book
     * @param bookId the unique identifier of the book being loaned
     * @param price the price of the loan
     * @param loanDate the date when the loan was made
     * @param dueDate the due date for the book's return
     * @param returnDate the actual return date of the book (null if not returned)
     * @param status the current status of the loan
     */
    public Loan(UUID id,
                UUID memberId,
                UUID bookId,
                double price,
                LocalDate loanDate,
                LocalDate dueDate,
                LocalDate returnDate,
                LoanStatus status) {
        super(id);
        this.memberId = memberId;
        this.bookId = bookId;
        this.price = price;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
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

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", bookId=" + bookId +
                ", price=" + price +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }
}
