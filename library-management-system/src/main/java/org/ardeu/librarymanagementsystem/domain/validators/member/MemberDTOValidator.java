package org.ardeu.librarymanagementsystem.domain.validators.member;

import org.ardeu.librarymanagementsystem.domain.entities.member.MemberCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Validator for validating {@link MemberCreationDTO} objects.
 * <p>
 * This class ensures that the input data for creating a new member is valid,
 * including checks for the member's name and email.
 */
public class MemberDTOValidator implements Validator<MemberCreationDTO> {

    /**
     * Regular expression for validating the format of an email address.
     */
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    /**
     * Validates the given {@link MemberCreationDTO}.
     *
     * @param memberDTO the MemberCreationDTO object to validate
     * @throws ValidationException if the validation fails (e.g., if the email is invalid or the name is too short)
     */
    @Override
    public void validate(MemberCreationDTO memberDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateName(memberDTO.name(), errors);
        validateEmail(memberDTO.email(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    /**
     * Validates the email address of the member.
     *
     * @param email the email address to validate
     * @param errors a list to collect error messages
     */
    private void validateEmail(String email, List<String> errors) {
        if (Objects.isNull(email)) {
            errors.add("Email cannot be empty");
        }
        if (!email.matches(EMAIL_REGEX)) {
            errors.add("Email is not valid");
        }
    }

    /**
     * Validates the name of the member.
     *
     * @param name the name of the member to validate
     * @param errors a list to collect error messages
     */
    private void validateName(String name, List<String> errors) {
        if (Objects.isNull(name)) {
            errors.add("Name cannot be empty");
        }
        if (name.length() < 2) {
            errors.add("Name must be at least 2 characters long");
        }
    }
}