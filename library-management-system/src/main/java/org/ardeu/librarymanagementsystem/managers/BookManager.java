package org.ardeu.librarymanagementsystem.managers;

import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.services.AuthorService;
import org.ardeu.librarymanagementsystem.services.BookService;
import org.ardeu.librarymanagementsystem.services.GenreService;
import org.ardeu.librarymanagementsystem.services.InventoryService;
import org.ardeu.librarymanagementsystem.services.registry.ServiceRegistry;

import java.util.UUID;

public class BookManager {
    private final BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private final GenreService genreService = ServiceRegistry.getInstance().getService(GenreService.class);
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);

    public void addBook(Book book, UUID genreId, UUID authorId, int totalCopies, double price){
        try {
            this.bookService.add(book);
            this.genreService.addBook(genreId, book);
            this.authorService.addBook(authorId, book);
            Inventory inventory = this.inventoryService.createInventory(book, totalCopies, price);
            this.inventoryService.add(inventory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
