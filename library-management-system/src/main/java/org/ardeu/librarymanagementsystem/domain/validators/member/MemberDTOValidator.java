package org.ardeu.librarymanagementsystem.domain.validators.member;

import org.ardeu.librarymanagementsystem.domain.entities.member.MemberDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemberDTOValidator implements Validator<MemberDTO> {
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    @Override
    public void validate(MemberDTO memberDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateName(memberDTO.name(), errors);
        validateEmail(memberDTO.email(), errors);

        if(!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    private void validateEmail(String email, List<String> errors) {
        if(Objects.isNull(email)) {
            errors.add("Email cannot be empty");
        }
        if(!email.matches(EMAIL_REGEX)) {
            errors.add("Email is not valid");
        }
    }

    private void validateName(String name, List<String> errors) {
        if(Objects.isNull(name)) {
            errors.add("Name cannot be empty");
        }
        if(name.length() < 2) {
            errors.add("Name must be at least 2 characters long");
        }
    }
}
