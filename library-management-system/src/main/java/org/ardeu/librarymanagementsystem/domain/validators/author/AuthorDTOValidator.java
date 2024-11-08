package org.ardeu.librarymanagementsystem.domain.validators.author;

import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validator for validating {@link AuthorCreationDTO} objects.
 * <p>
 * This class is responsible for ensuring that the input data for creating an author is valid,
 * including checking if the author's name is not null or empty.
 */
public class AuthorDTOValidator implements Validator<AuthorCreationDTO> {

    /**
     * Validates the given {@link AuthorCreationDTO}.
     *
     * @param authorCreationDTO the AuthorCreationDTO object to validate
     * @throws ValidationException if the validation fails (e.g., if the name is null or empty)
     */
    @Override
    public void validate(AuthorCreationDTO authorCreationDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateName(authorCreationDTO.name(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    /**
     * Validates the author's name.
     *
     * @param name the name of the author to validate
     * @param errors a list to collect error messages
     */
    private void validateName(String name, List<String> errors) {
        if (Objects.isNull(name)) {
            errors.add("Name cannot be null");
        }
        if (name.isEmpty()) {
            errors.add("Name cannot be empty");
        }
    }
}
