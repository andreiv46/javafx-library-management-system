package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.InventoryDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.InventoryNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class InventoryService extends Service<Inventory> {

    public InventoryService(MapFileHandler<UUID, Inventory> fileHandler) {
        super(fileHandler);
    }

    public Inventory getByBookId(UUID bookId) throws InventoryNotFoundException {
        for(Inventory inventory : items.values()){
            if(inventory.getBookid().equals(bookId)){
                return inventory;
            }
        }
        throw new InventoryNotFoundException("Inventory for book with ID " + bookId + " not found");
    }

    public Inventory create(InventoryDTO inventoryDTO) {
        return new Inventory(
                UUID.randomUUID(),
                inventoryDTO.bookId(),
                inventoryDTO.totalCopies(),
                inventoryDTO.availableCopies(),
                inventoryDTO.price()
        );
    }

    public void remove(Inventory inventory) {
        super.items.remove(inventory.getId());
    }

    public List<Book> getAvailableBooks(List<Book> books) {
        return books.stream()
                .filter(book -> {
                    try {
                        Inventory inventory = getByBookId(book.getId());
                        return inventory.getAvailableCopies() > 0;
                    } catch (InventoryNotFoundException e) {
                        return false;
                    }
                })
                .toList();
    }

    public void updateInventory(UUID bookId) throws InventoryNotFoundException {
        Inventory inventory = items.values().stream()
                .filter(i -> i.getBookid().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new InventoryNotFoundException("Inventory for book with ID " + bookId + " not found"));
        inventory.setAvailableCopies(inventory.getAvailableCopies() - 1);
        items.put(inventory.getId(), inventory);
    }

    public void revertInventoryUpdate(UUID bookId) {
        if(Objects.isNull(bookId)){
            return;
        }
        for (Inventory inventory : items.values()) {
            if (inventory.getBookid().equals(bookId)) {
                inventory.setAvailableCopies(inventory.getAvailableCopies() + 1);
                items.put(inventory.getId(), inventory);
                break;
            }
        }
    }
}
