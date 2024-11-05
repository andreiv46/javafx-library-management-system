package org.ardeu.librarymanagementsystem.domain.validators.inventory;

import org.ardeu.librarymanagementsystem.domain.entities.inventory.InventoryDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class InventoryDTOValidator implements Validator<InventoryDTO> {

    @Override
    public void validate(InventoryDTO inventoryDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();

        validateBookId(inventoryDTO.bookId(), errors);
        validateTotalCopies(inventoryDTO.totalCopies(), errors);
        validateAvailableCopies(inventoryDTO.availableCopies(), inventoryDTO.totalCopies() , errors);
        validatePrice(inventoryDTO.price(), errors);
    }

    private void validatePrice(double price, List<String> errors) {
        if(price < 0){
            errors.add("Price cannot be negative");
        }
    }

    private void validateAvailableCopies(int availableCopies, int totalCopies ,List<String> errors) {
        if(availableCopies < 0){
            errors.add("Available copies cannot be negative");
        }
        if(availableCopies > totalCopies){
            errors.add("Available copies cannot be greater than total copies");
        }
    }

    private void validateTotalCopies(int copies, List<String> errors) {
        if(copies < 0){
            errors.add("Total copies cannot be negative");
        }
    }

    private void validateBookId(UUID uuid, List<String> errors) {
        if(Objects.isNull(uuid)){
            errors.add("Book ID cannot be null");
        }
    }
}
