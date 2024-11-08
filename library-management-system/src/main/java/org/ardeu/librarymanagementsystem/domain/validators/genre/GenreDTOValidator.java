package org.ardeu.librarymanagementsystem.domain.validators.genre;

import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validator for validating {@link GenreCreationDTO} objects.
 * <p>
 * This class is responsible for ensuring that the input data for creating a genre is valid,
 * including checks for the name of the genre.
 */
public class GenreDTOValidator implements Validator<GenreCreationDTO> {

    /**
     * Validates the given {@link GenreCreationDTO}.
     *
     * @param genreDTO the GenreCreationDTO object to validate
     * @throws ValidationException if the validation fails (e.g., if the name is null, empty, too short, or too long)
     */
    @Override
    public void validate(GenreCreationDTO genreDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateName(genreDTO.name(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    /**
     * Validates the genre's name.
     *
     * @param name the name of the genre to validate
     * @param errors a list to collect error messages
     */
    private void validateName(String name, List<String> errors) {
        if (Objects.isNull(name)) {
            errors.add("Name cannot be null");
        }
        if (name.isBlank()) {
            errors.add("Name cannot be empty");
        }
        if (name.length() < 2) {
            errors.add("Name cannot be shorter than 2 characters");
        }
        if (name.length() > 255) {
            errors.add("Name cannot be longer than 255 characters");
        }
    }
}
