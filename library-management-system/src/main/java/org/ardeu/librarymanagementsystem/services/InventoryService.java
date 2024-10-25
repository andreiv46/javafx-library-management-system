package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;
import org.ardeu.librarymanagementsystem.services.base.Service;

import java.util.UUID;

public class InventoryService extends Service<Inventory> {

    public InventoryService(FileHandler<Inventory> fileHandler) {
        super(fileHandler);
    }

    public Inventory findByBookId(UUID bookId){
        for(Inventory inventory : items.values()){
            if(inventory.getBookid().equals(bookId)){
                return inventory;
            }
        }
        return null;
    }

    public Inventory createInventory(Book book, int totalCopies, double price) {
        return new Inventory(
                UUID.randomUUID(),
                book.getId(),
                totalCopies,
                totalCopies,
                price
        );
    }
}
