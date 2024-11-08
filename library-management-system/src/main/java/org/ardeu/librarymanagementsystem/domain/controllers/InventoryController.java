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

/**
 * InventoryController is responsible for handling inventory-related operations.
 */
public class InventoryController {
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);
    private final BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);

    /**
     * Retrieves the inventory for a specific book by its ID.
     *
     * @param bookId the UUID of the book
     * @return a Result containing the Inventory object if found, or a failure message if not found
     */
    public Result<Inventory> getInventoryByBookId(UUID bookId){
        try {
            Inventory inventory;
            inventory = this.inventoryService.getByBookId(bookId);
            return Result.success(inventory);
        } catch (InventoryNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Retrieves a list of all available books in the inventory.
     *
     * @return a Result containing a list of available books, or a failure message if an error occurs
     */
    public Result<List<Book>> getAvailableBooks(){
        List<Book> allBooks = this.bookService.getItems().values().stream().toList();
        List<Book> availableBooks = this.inventoryService.getAvailableBooks(allBooks);
        return Result.success(availableBooks);
    }

    /**
     * Retrieves all inventories from the system.
     *
     * @return a Result containing an ObservableMap of inventories, or a failure message if an error occurs
     */
    public Result<ObservableMap<UUID, Inventory>> getAllInventories() {
        return Result.success(this.inventoryService.getItems());
    }
}
