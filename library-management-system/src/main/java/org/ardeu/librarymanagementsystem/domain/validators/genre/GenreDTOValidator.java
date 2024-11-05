package org.ardeu.librarymanagementsystem.domain.validators.genre;

import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GenreDTOValidator implements Validator<GenreDTO> {
    @Override
    public void validate(GenreDTO genreDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateName(genreDTO.name(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }
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
        if(name.length() > 255){
            errors.add("Name cannot be longer than 255 characters");
        }
    }
}
