package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreExportField;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.GenreService;
import org.ardeu.librarymanagementsystem.domain.services.LoanService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.genre.GenreDTOValidator;

import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * GenreController is responsible for handling genre-related operations.
 */
public class GenreController {
    private final GenreService genreService = ServiceRegistry.getInstance().getService(GenreService.class);
    private final LoanService loanService = ServiceRegistry.getInstance().getService(LoanService.class);
    private final Validator<GenreCreationDTO> genreDTOValidator;

    /**
     * Constructor that initializes the GenreDTOValidator.
     */
    public GenreController() {
        this.genreDTOValidator = new GenreDTOValidator();
    }

    /**
     * Retrieves a genre by its ID.
     *
     * @param id the UUID of the genre
     * @return a Result containing the Genre object if found, or a failure message if not found
     */
    public Result<Genre> getGenreById(UUID id){
        try {
            Genre genre = this.genreService.getById(id);
            return Result.success(genre);
        } catch (GenreNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Retrieves all genres from the system.
     *
     * @return a Result containing an ObservableMap of genres, or a failure message if an error occurs
     */
    public Result<ObservableMap<UUID, Genre>> getAllGenres() {
        ObservableMap<UUID, Genre> genres = this.genreService.getItems();
        return Result.success(genres);
    }

    /**
     * Adds a new genre to the system.
     *
     * @param genreDTO the data transfer object containing genre creation details
     * @return a Result containing the created Genre, or a failure message if validation fails or the genre already exists
     */
    public Result<Genre> addGenre(GenreCreationDTO genreDTO) {
        try {
            this.genreDTOValidator.validate(genreDTO);
            this.genreService.checkGenreExistsByName(genreDTO.name());
            Genre genre = this.genreService.create(genreDTO);
            this.genreService.add(genre);
            return Result.success(genre);
        } catch (ValidationException e) {
            return Result.failure("Validation failed: " + e.getMessage());
        } catch (GenreAlreadyExistsException | DuplicateItemException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Retrieves the total number of loans for books by a specific genre, identified by genre ID.
     *
     * @param genreId the UUID of the genre to retrieve loan counts for
     * @return a Result containing the total number of loans for books by the specified genre, or a failure message if the genre is not found
     */
    public Result<Long> getLoansCount(UUID genreId) {
        try {
            Set<UUID> books = this.genreService.getBookIds(genreId);
            this.loanService.getTotalLoansByBookId(books);
            long loansCount = this.loanService.getTotalLoansByBookId(books);
            return Result.success(loansCount);
        } catch (GenreNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Exports the genre data to a CSV file with a specified set of fields.
     *
     * @param file the file to export to
     * @param genreList the list of genres to export
     * @param checkModel the fields to include in the export
     * @return a Result indicating success or failure of the export operation
     */
    public Result<Void> exportGenresToCSV(File file, List<Genre> genreList, List<GenreExportField> checkModel) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(checkModel.stream().map(GenreExportField::getDisplayName).toArray(String[]::new))
                .build();

        try(CSVPrinter printer =
                    new CSVPrinter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            new FileOutputStream(file))), csvFormat)) {

            for (Genre genre : genreList) {
                double booksCount = this.genreService.getBooksCount(genre.getId());
                Set<UUID> books = this.genreService.getBookIds(genre.getId());

                printer.printRecord(
                        checkModel.stream().map(field -> switch (field) {
                            case ID -> genre.getId();
                            case NAME -> genre.getName();
                            case BOOKS_COUNT -> booksCount;
                            case TOTAL_LOANS -> this.loanService.getTotalLoansByBookId(books);
                        }).toArray());
            }
            return Result.success();
        } catch (IOException e) {
            return Result.failure("Error exporting genres to CSV");
        } catch (GenreNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }
}
