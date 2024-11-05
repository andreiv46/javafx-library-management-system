package org.ardeu.librarymanagementsystem.domain.validators.author;

import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AuthorDTOValidator implements Validator<AuthorDTO> {
    @Override
    public void validate(AuthorDTO authorDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateName(authorDTO.name(), errors);

        if(!errors.isEmpty()){
            throw new ValidationException(String.join("\n", errors));
        }
    }

    private void validateName(String name, List<String> errors) {
        if(Objects.isNull(name)){
            errors.add("Name cannot be null");
        }
        if(name.isEmpty()){
            errors.add("Name cannot be empty");
        }
    }
}
