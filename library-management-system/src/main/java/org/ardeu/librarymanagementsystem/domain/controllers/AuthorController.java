package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorExportField;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.AuthorService;
import org.ardeu.librarymanagementsystem.domain.services.LoanService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.author.AuthorDTOValidator;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * AuthorController is responsible for handling author-related operations.
 */
public class AuthorController {
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private final LoanService loanService = ServiceRegistry.getInstance().getService(LoanService.class);
    private final Validator<AuthorCreationDTO> authorDTOValidator;

    /**
     * Constructor that initializes the AuthorDTOValidator.
     */
    public AuthorController() {
        authorDTOValidator = new AuthorDTOValidator();
    }

    /**
     * Retrieves all authors from the system.
     *
     * @return a Result containing an ObservableMap of authors, or a failure message if an error occurs
     */
    public Result<ObservableMap<UUID, Author>> getAllAuthors() {
        ObservableMap<UUID, Author> authors = this.authorService.getItems();
        return Result.success(authors);
    }

    /**
     * Retrieves an author by its ID.
     *
     * @param id the UUID of the author
     * @return a Result containing the Author object if found, or a failure message if not found
     */
    public Result<Author> getAuthorById(UUID id) {
        try {
            Author author = this.authorService.getById(id);
            return Result.success(author);
        } catch (AuthorNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Adds a new author to the system.
     *
     * @param authorCreationDTO the data transfer object containing author creation details
     * @return a Result containing the created Author, or a failure message if validation fails or the author already exists
     */
    public Result<Author> addAuthor(AuthorCreationDTO authorCreationDTO) {
        try {
            this.authorDTOValidator.validate(authorCreationDTO);
            this.authorService.authorExistsByName(authorCreationDTO.name());
            Author author = this.authorService.create(authorCreationDTO);
            this.authorService.add(author);
            return Result.success(author);
        } catch (ValidationException e) {
            return Result.failure("Validation failed: \n" + e.getMessage());
        } catch (AuthorAlreadyExistsException | DuplicateItemException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Retrieves the total number of loans for books by a specific author, identified by genre ID.
     *
     * @param genreId the UUID of the genre to retrieve loan counts for
     * @return a Result containing the total number of loans for books by the specified author, or a failure message if the author is not found
     */
    public Result<Long> getLoansCount(UUID genreId) {
        try {
            Set<UUID> books = this.authorService.getBookIds(genreId);
            this.loanService.getTotalLoansByBookId(books);
            long loansCount = this.loanService.getTotalLoansByBookId(books);
            return Result.success(loansCount);
        } catch (AuthorNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Exports the author data to a CSV file with a specified set of fields.
     *
     * @param file the file to export to
     * @param authorList the list of authors to export
     * @param checkModel the fields to include in the export
     * @return a Result indicating success or failure of the export operation
     */
    public Result<Void> exportAuthorsToCSV(File file, List<Author> authorList, List<AuthorExportField> checkModel) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(checkModel.stream().map(AuthorExportField::getDisplayName).toArray(String[]::new))
                .build();

        try (CSVPrinter printer =
                     new CSVPrinter(
                             new BufferedWriter(
                                     new OutputStreamWriter(
                                             new FileOutputStream(file))), csvFormat)) {
            for (Author author : authorList) {
                double booksCount = this.authorService.getBooksCount(author.getId());
                Set<UUID> bookIds = this.authorService.getBookIds(author.getId());
                printer.printRecord(
                        checkModel.stream()
                                .map(field -> switch (field) {
                                    case ID -> author.getId();
                                    case NAME -> author.getName();
                                    case BOOKS_COUNT -> booksCount;
                                    case TOTAL_LOANS -> this.loanService.getTotalLoansByBookId(bookIds);
                                }).toArray());
            }
            return Result.success();
        } catch (IOException e) {
            return Result.failure("Error exporting authors to CSV");
        } catch (AuthorNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }
}

