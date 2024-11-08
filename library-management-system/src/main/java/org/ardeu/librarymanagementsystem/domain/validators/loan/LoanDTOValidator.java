package org.ardeu.librarymanagementsystem.domain.validators.loan;

import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Validator for validating {@link LoanCreationDTO} objects.
 * <p>
 * This class ensures that the input data for creating a loan entry is valid,
 * including checks for the book ID, member ID, due date, and price.
 */
public class LoanDTOValidator implements Validator<LoanCreationDTO> {

    /**
     * Validates the given {@link LoanCreationDTO}.
     *
     * @param loanDTO the LoanCreationDTO object to validate
     * @throws ValidationException if the validation fails (e.g., if the book ID is null, the due date is not in the future, etc.)
     */
    @Override
    public void validate(LoanCreationDTO loanDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateBookId(loanDTO.bookId(), errors);
        validateMemberId(loanDTO.memberId(), errors);
        validateDueDate(loanDTO.dueDate(), errors);
        validatePrice(loanDTO.price(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    /**
     * Validates the price of the loan.
     *
     * @param price the price of the loan to validate
     * @param errors a list to collect error messages
     */
    private void validatePrice(double price, List<String> errors) {
        if (price < 0) {
            errors.add("Price must be greater than or equal to 0");
        }
    }

    /**
     * Validates the due date of the loan.
     *
     * @param localDate the due date to validate
     * @param errors a list to collect error messages
     */
    private void validateDueDate(LocalDate localDate, List<String> errors) {
        if (Objects.isNull(localDate)) {
            errors.add("Due date is required");
        }
        if (localDate.isBefore(LocalDate.now()) || localDate.isEqual(LocalDate.now())) {
            errors.add("Due date must be in the future");
        }
    }

    /**
     * Validates the member ID associated with the loan.
     *
     * @param uuid the member ID to validate
     * @param errors a list to collect error messages
     */
    private void validateMemberId(UUID uuid, List<String> errors) {
        if (Objects.isNull(uuid)) {
            errors.add("Member id is required");
        }
    }

    /**
     * Validates the book ID associated with the loan.
     *
     * @param id the book ID to validate
     * @param errors a list to collect error messages
     */
    private void validateBookId(UUID id, List<String> errors) {
        if (Objects.isNull(id)) {
            errors.add("Book id is required");
        }
    }
}

