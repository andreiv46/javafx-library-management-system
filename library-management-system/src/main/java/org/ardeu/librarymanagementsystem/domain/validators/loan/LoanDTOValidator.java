package org.ardeu.librarymanagementsystem.domain.validators.loan;

import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class LoanDTOValidator implements Validator<LoanDTO> {
    @Override
    public void validate(LoanDTO loanDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateBookId(loanDTO.bookId(), errors);
        validateMemberId(loanDTO.memberId(), errors);
        validateDueDate(loanDTO.dueDate(), errors);
        validatePrice(loanDTO.price(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    private void validatePrice(double price, List<String> errors) {
        if (price < 0) {
            errors.add("Price must be greater than or equal to 0");
        }
    }

    private void validateDueDate(LocalDate localDate, List<String> errors) {
        if (Objects.isNull(localDate)) {
            errors.add("Due date is required");
        }
        if (localDate.isBefore(LocalDate.now()) || localDate.isEqual(LocalDate.now())) {
            errors.add("Due date must be in the future");
        }
    }

    private void validateMemberId(UUID uuid, List<String> errors) {
        if (Objects.isNull(uuid)) {
            errors.add("Member id is required");
        }
    }

    private void validateBookId(UUID id, List<String> errors) {
        if (Objects.isNull(id)) {
            errors.add("Book id is required");
        }
    }
}
