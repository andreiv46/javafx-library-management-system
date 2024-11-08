package org.ardeu.librarymanagementsystem.domain.entities.inventory.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;

import java.util.UUID;
import java.util.function.Function;

/**
 * This class is responsible for mapping a CSV line (String) to an Inventory object.
 * It implements the {@link Function} interface and converts a comma-separated string into an Inventory entity.
 */
public class CSVToInventoryMapper implements Function<String, Inventory> {
    /**
     * Converts a CSV string into an {@link Inventory} object.
     * The CSV string should be in the format:
     * ID, BookID, AvailableCopies, TotalCopies, Price
     *
     * @param s the CSV string to be mapped to an Inventory object
     * @return the corresponding Inventory object
     * @throws NumberFormatException if any of the numeric fields are not valid numbers
     * @throws IllegalArgumentException if the CSV string format is invalid
     */
    @Override
    public Inventory apply(String s) {
        String[] values = s.split(",");
        if (values.length != 5) {
            throw new IllegalArgumentException("Invalid CSV format for Inventory");
        }
        return new Inventory(
                UUID.fromString(values[0]),
                UUID.fromString(values[1]),
                Integer.parseInt(values[2]),
                Integer.parseInt(values[3]),
                Double.parseDouble(values[4])
        );
    }
}
