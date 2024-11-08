package org.ardeu.librarymanagementsystem.domain.validators.inventory;

import org.ardeu.librarymanagementsystem.domain.entities.inventory.InventoryCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Validator for validating {@link InventoryCreationDTO} objects.
 * <p>
 * This class ensures that the input data for creating an inventory entry is valid,
 * including checks for the book ID, total copies, available copies, and price.
 */
public class InventoryDTOValidator implements Validator<InventoryCreationDTO> {

    /**
     * Validates the given {@link InventoryCreationDTO}.
     *
     * @param inventoryDTO the InventoryCreationDTO object to validate
     * @throws ValidationException if the validation fails (e.g., if the book ID is null, the price is negative, etc.)
     */
    @Override
    public void validate(InventoryCreationDTO inventoryDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateBookId(inventoryDTO.bookId(), errors);
        validateTotalCopies(inventoryDTO.totalCopies(), errors);
        validateAvailableCopies(inventoryDTO.availableCopies(), inventoryDTO.totalCopies(), errors);

        validatePrice(inventoryDTO.price(), errors);
        if (!errors.isEmpty()) {
            throw new ValidationException(String.join("\n", errors));
        }
    }

    /**
     * Validates the price of the inventory.
     *
     * @param price the price of the inventory item to validate
     * @param errors a list to collect error messages
     */
    private void validatePrice(double price, List<String> errors) {
        if (price < 0) {
            errors.add("Price cannot be negative");
        }
    }

    /**
     * Validates the available copies of the book against the total copies.
     *
     * @param availableCopies the available copies of the book
     * @param totalCopies the total copies of the book
     * @param errors a list to collect error messages
     */
    private void validateAvailableCopies(int availableCopies, int totalCopies, List<String> errors) {
        if (availableCopies < 0) {
            errors.add("Available copies cannot be negative");
        }
        if (availableCopies > totalCopies) {
            errors.add("Available copies cannot be greater than total copies");
        }
    }

    /**
     * Validates the total copies of the book.
     *
     * @param copies the total copies of the book to validate
     * @param errors a list to collect error messages
     */
    private void validateTotalCopies(int copies, List<String> errors) {
        if (copies < 0) {
            errors.add("Total copies cannot be negative");
        }
    }

    /**
     * Validates the book ID.
     *
     * @param uuid the book ID to validate
     * @param errors a list to collect error messages
     */
    private void validateBookId(UUID uuid, List<String> errors) {
        if (Objects.isNull(uuid)) {
            errors.add("Book ID cannot be null");
        }
    }
}

