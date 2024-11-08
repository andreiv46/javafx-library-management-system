package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanExportField;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanStatus;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.InventoryNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.NoAvailableCopiesException;
import org.ardeu.librarymanagementsystem.domain.exceptions.loan.InvalidReturnDateException;
import org.ardeu.librarymanagementsystem.domain.exceptions.loan.LoanNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.member.MemberNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.*;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.loan.LoanDTOValidator;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

/**
 * LoanController is responsible for handling loan-related operations.
 */
public class LoanController {
    private final LoanService loanService = ServiceRegistry.getInstance().getService(LoanService.class);
    private final MemberService memberService = ServiceRegistry.getInstance().getService(MemberService.class);
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);
    private final BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);
    private final GenreService genreService = ServiceRegistry.getInstance().getService(GenreService.class);
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private final Validator<LoanCreationDTO> loanValidator = new LoanDTOValidator();

    /**
     * Retrieves all loans from the system.
     *
     * @return a Result containing an ObservableMap of loans, or a failure message if an error occurs
     */
    public Result<ObservableMap<UUID, Loan>> getAllLoans() {
        ObservableMap<UUID, Loan> loans = loanService.getItems();
        return Result.success(loans);
    }

    /**
     * Adds multiple loans for a user.
     *
     * @param loanDTOS the list of LoanCreationDTO objects containing loan creation details
     * @param memberId the UUID of the member
     * @return a Result containing a list of created Loan objects, or a failure message if validation fails or an error occurs
     */
    public Result<List<Loan>> addLoansForUser(List<LoanCreationDTO> loanDTOS, UUID memberId) {
        List<Loan> loans = new ArrayList<>();

        try {
            // Validate loanDto
            for (LoanCreationDTO loanDTO : loanDTOS) {
                this.loanValidator.validate(loanDTO);
            }

            // Create loans and update inventory
            for (LoanCreationDTO loanDTO : loanDTOS) {
                Loan loan = loanService.create(loanDTO);
                loans.add(loan);
                this.inventoryService.borrowBook(loan.getBookId());
                this.memberService.addLoanToMember(memberId, loan.getId());
                this.loanService.add(loan);
            }

            return Result.success(loans);
        }
        catch (ValidationException e) {
            return Result.failure("Validation failed:\n" + e.getMessage());
        } catch (MemberNotFoundException | InventoryNotFoundException | DuplicateItemException e) {
            revertAddLoans(loans);
            return Result.failure(e.getMessage());
        } catch (NoAvailableCopiesException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Reverts the addition of loans in case of an error.
     *
     * @param loans the list of loans to revert
     */
    private void revertAddLoans(List<Loan> loans) {
        for (Loan loan : loans) {
            this.inventoryService.revertInventoryUpdate(loan.getBookId());
            this.loanService.remove(loan.getId());
            this.memberService.removeLoanFromMember(loan.getMemberId(), loan.getId());
        }
    }

    /**
     * Handles the return of a loan.
     *
     * @param id the UUID of the loan
     * @param value the return date
     * @return a Result containing the updated Loan object, or a failure message if an error occurs
     */
    public Result<Loan> handleLoanReturn(UUID id, LocalDate value) {
        Loan loan = null;
        try {
            loan = this.loanService.getById(id);
            this.loanService.markLoanAsReturned(loan, value);
            this.inventoryService.returnBook(loan.getBookId());
            return Result.success(loan);
        } catch (LoanNotFoundException | InvalidReturnDateException e) {
            return Result.failure(e.getMessage());
        } catch (InventoryNotFoundException e) {
            this.loanService.markLoanAsBorrowed(loan);
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Retrieves the revenue for the last 30 days.
     *
     * @return a Result containing the revenue for the last 30 days
     */
    public Result<Double> getRevenueForLast30Days() {
        return Result.success(this.loanService.getRevenueForLast30Days());
    }

    /**
     * Retrieves the all-time revenue.
     *
     * @return a Result containing the all-time revenue
     */
    public Result<Double> getAllTimeRevenue() {
        return Result.success(this.loanService.getAllTimeRevenue());
    }

    /**
     * Retrieves the revenue for the past year.
     *
     * @return a Result containing the revenue for the past year
     */
    public Result<Double> getRevenueForPastYear() {
        return Result.success(this.loanService.getRevenueForPastYear());
    }

    /**
     * Retrieves loans from a specific year.
     *
     * @param year the year to retrieve loans from
     * @return a Result containing a list of loans from the specified year
     */
    public Result<List<Loan>> getLoansFrom(int year) {
        return Result.success(this.loanService.getLoansFrom(year));
    }

    /**
     * Retrieves the revenue per year.
     *
     * @return a Result containing a map of year to revenue
     */
    public Result<Map<Integer, Double>> getRevenuePerYear() {
        return Result.success(this.loanService.getRevenuePerYear());
    }

    /**
     * Exports the loan data to a CSV file with a specified set of fields.
     *
     * @param file the file to export to
     * @param loanList the list of loans to export
     * @param checkModel the fields to include in the export
     * @return a Result indicating success or failure of the export operation
     */
    public Result<Void> exportLoansToCSV(File file, List<Loan> loanList, List<LoanExportField> checkModel) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(checkModel.stream().map(LoanExportField::getDisplayName).toArray(String[]::new))
                .build();

        try(CSVPrinter printer =
                    new CSVPrinter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            new FileOutputStream(file))), csvFormat)) {
            for (Loan loan : loanList) {
                Member member = this.memberService.getById(loan.getMemberId());
                Book book = this.bookService.getById(loan.getBookId());
                Genre genre = this.genreService.getById(book.getGenreId());
                Author author = this.authorService.getById(book.getAuthorId());
                printer.printRecord(
                        checkModel.stream()
                                .map(field -> switch (field) {
                                    case ID -> loan.getId();
                                    case BOOK_ID -> loan.getBookId();
                                    case BOOK_TITLE -> book.getTitle();
                                    case MEMBER_ID -> loan.getMemberId();
                                    case MEMBER_NAME -> member.getName();
                                    case MEMBER_EMAIL -> member.getEmail();
                                    case LOAN_DATE -> loan.getLoanDate();
                                    case DUE_DATE -> loan.getDueDate();
                                    case RETURN_DATE -> loan.getReturnDate();
                                    case STATUS -> loan.getStatus();
                                    case PRICE -> loan.getPrice();
                                    case GENRE_ID -> book.getGenreId();
                                    case GENRE_NAME -> genre.getName();
                                    case AUTHOR_ID -> book.getAuthorId();
                                    case AUTHOR_NAME -> author.getName();
                                }).toArray()
                );
            }
            return Result.success();
        } catch (IOException e) {
            return Result.failure("Error exporting authors to CSV");
        } catch (MemberNotFoundException | BookNotFoundException | GenreNotFoundException | AuthorNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Deletes a loan by its ID.
     *
     * @param id the UUID of the loan
     * @return a Result containing the UUID of the deleted loan, or a failure message if an error occurs
     */
    public Result<UUID> deleteLoanById(UUID id) {
        try {
            Loan loan = this.loanService.getById(id);

            if (LoanStatus.ACTIVE.equals(loan.getStatus())) {
                this.inventoryService.returnBook(loan.getBookId());
            }

            this.loanService.remove(id);
            this.memberService.removeLoanFromMember(loan.getMemberId(), id);

            return Result.success(loan.getId());
        } catch (LoanNotFoundException | InventoryNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }
}
