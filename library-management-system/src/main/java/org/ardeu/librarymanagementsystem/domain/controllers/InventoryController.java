package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.InventoryNotFoundException;
import org.ardeu.librarymanagementsystem.domain.services.BookService;
import org.ardeu.librarymanagementsystem.domain.services.InventoryService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;

import java.util.List;
import java.util.UUID;

public class InventoryController {
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);
    private final BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);

    public Result<Inventory> getInventoryByBookId(UUID bookId){
        try {
            Inventory inventory;
            inventory = this.inventoryService.getByBookId(bookId);
            return Result.success(inventory);
        } catch (InventoryNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<List<Book>> getAvailableBooks(){
        List<Book> allBooks = this.bookService.getItems().values().stream().toList();
        List<Book> availableBooks = this.inventoryService.getAvailableBooks(allBooks);
        return Result.success(availableBooks);
    }

    public Result<ObservableMap<UUID, Inventory>> getAllInventories() {
        return Result.success(this.inventoryService.getItems());
    }
}
