package org.ardeu.librarymanagementsystem.domain.entities.inventory.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;

import java.util.function.Function;

public class InventoryToCSVMapper implements Function<Inventory, String> {
    @Override
    public String apply(Inventory inventory) {
        return String.join(",",
                inventory.getId().toString(),
                inventory.getBookid().toString(),
                String.valueOf(inventory.getAvailableCopies()),
                String.valueOf(inventory.getTotalCopies()),
                String.valueOf(inventory.getPrice())
        );
    }
}
