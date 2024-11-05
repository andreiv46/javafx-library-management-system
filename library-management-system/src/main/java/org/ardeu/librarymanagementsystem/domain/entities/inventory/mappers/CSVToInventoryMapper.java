package org.ardeu.librarymanagementsystem.domain.entities.inventory.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;

import java.util.UUID;
import java.util.function.Function;

public class CSVToInventoryMapper implements Function<String, Inventory> {
    @Override
    public Inventory apply(String s) {
        String[] values = s.split(",");
        return new Inventory(
                UUID.fromString(values[0]),
                UUID.fromString(values[1]),
                Integer.parseInt(values[2]),
                Integer.parseInt(values[3]),
                Double.parseDouble(values[4])
        );
    }
}
