package org.ardeu.librarymanagementsystem.domain.validators.book;

import org.ardeu.librarymanagementsystem.domain.entities.book.BookCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Validator for validating {@link BookCreationDTO} objects.
 * <p>
 * This class is responsible for ensuring that the input data for creating a book is valid,
 * including checks for title, description, author ID, genre ID, and publication date.
 */
public class BookDTOValidator implements Validator<BookCreationDTO> {

    /**
     * Validates the given {@link BookCreationDTO}.
     *
     * @param bookDTO the BookCreationDTO object to validate
     * @throws ValidationException if the validation fails (e.g., if the title is null or empty)
     */
    @Override
    public void validate(BookCreationDTO bookDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateTitle(bookDTO.title(), errors);
        validateDescription(bookDTO.description(), errors);
        validateAuthorId(bookDTO.authorId(), errors);
        validateGenreId(bookDTO.genreId(), errors);
        validatePublicationDate(bookDTO.releaseDate(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    /**
     * Validates the book's title.
     *
     * @param title the title of the book to validate
     * @param errors a list to collect error messages
     */
    private void validateTitle(String title, List<String> errors) {
        if (Objects.isNull(title)) {
            errors.add("Title cannot be null");
        }
        if (title.isBlank()) {
            errors.add("Title cannot be empty");
        }
    }

    /**
     * Validates the book's description.
     *
     * @param description the description of the book to validate
     * @param errors a list to collect error messages
     */
    private void validateDescription(String description, List<String> errors) {
        if (Objects.isNull(description)) {
            errors.add("Description cannot be null");
        }
        if (description.isBlank()) {
            errors.add("Description cannot be empty");
        }
    }

    /**
     * Validates the author's ID.
     *
     * @param authorId the ID of the author to validate
     * @param errors a list to collect error messages
     */
    private void validateAuthorId(UUID authorId, List<String> errors) {
        if (Objects.isNull(authorId)) {
            errors.add("Author ID cannot be null");
        }
    }

    /**
     * Validates the genre's ID.
     *
     * @param genreId the ID of the genre to validate
     * @param errors a list to collect error messages
     */
    private void validateGenreId(UUID genreId, List<String> errors) {
        if (Objects.isNull(genreId)) {
            errors.add("Genre ID cannot be null");
        }
    }

    /**
     * Validates the book's publication date.
     *
     * @param localDate the publication date of the book to validate
     * @param errors a list to collect error messages
     */
    private void validatePublicationDate(LocalDate localDate, List<String> errors) {
        if (Objects.isNull(localDate)) {
            errors.add("Publication date cannot be null");
        }
        if (localDate.isAfter(LocalDate.now())) {
            errors.add("Publication date cannot be in the future");
        }
    }
}

