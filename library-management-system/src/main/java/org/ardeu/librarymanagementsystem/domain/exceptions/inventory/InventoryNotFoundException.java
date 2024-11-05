package org.ardeu.librarymanagementsystem.domain.exceptions.inventory;

public class InventoryNotFoundException extends Exception {
    public InventoryNotFoundException(String message) {
        super(message);
    }
}
