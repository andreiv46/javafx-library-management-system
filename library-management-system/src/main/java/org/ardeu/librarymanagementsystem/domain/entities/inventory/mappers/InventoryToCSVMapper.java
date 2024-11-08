package org.ardeu.librarymanagementsystem.domain.entities.inventory.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;

import java.util.function.Function;

/**
 * This class is responsible for mapping an {@link Inventory} object to a CSV string.
 * It implements the {@link Function} interface and converts an Inventory entity into a comma-separated string.
 */
public class InventoryToCSVMapper implements Function<Inventory, String> {
    /**
     * Converts an {@link Inventory} object into a CSV string.
     * The resulting string will be in the following format:
     * ID, BookID, AvailableCopies, TotalCopies, Price
     *
     * @param inventory the Inventory object to be converted to a CSV string
     * @return the corresponding CSV string representation of the Inventory object
     */
    @Override
    public String apply(Inventory inventory) {
        return String.join(",",
                inventory.getId().toString(),
                inventory.getBookId().toString(),
                String.valueOf(inventory.getAvailableCopies()),
                String.valueOf(inventory.getTotalCopies()),
                String.valueOf(inventory.getPrice())
        );
    }
}
