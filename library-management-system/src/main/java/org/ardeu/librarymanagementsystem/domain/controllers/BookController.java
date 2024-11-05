package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.BookDTO;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.InventoryDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.AuthorService;
import org.ardeu.librarymanagementsystem.domain.services.BookService;
import org.ardeu.librarymanagementsystem.domain.services.GenreService;
import org.ardeu.librarymanagementsystem.domain.services.InventoryService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.book.BookDTOValidator;
import org.ardeu.librarymanagementsystem.domain.validators.inventory.InventoryDTOValidator;

import java.util.Objects;
import java.util.UUID;

public class BookController {
    private final BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private final GenreService genreService = ServiceRegistry.getInstance().getService(GenreService.class);
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);
    private final Validator<BookDTO> bookValidator = new BookDTOValidator();
    private final Validator<InventoryDTO> inventoryValidator = new InventoryDTOValidator();

    public Result<ObservableMap<UUID, Book>> getAllBooks(){
        ObservableMap<UUID, Book> books = this.bookService.getItems();
        return Result.success(books);
    }

    public Result<Book> addBook(BookDTO bookDTO, int totalCopies, double price) {
        Book book = null;
        Inventory inventory = null;
        try {
            this.bookValidator.validate(bookDTO);
            book = this.bookService.create(bookDTO);

            InventoryDTO inventoryDTO = new InventoryDTO(book.getId(), totalCopies, totalCopies, price);
            inventory = this.inventoryService.create(inventoryDTO);
            this.inventoryValidator.validate(inventoryDTO);

            this.bookService.checkBookExists(book);

            this.bookService.add(book);
            this.inventoryService.add(inventory);
            this.authorService.addBook(book.getAuthorId(), book);
            this.genreService.addBook(book.getGenreId(), book);

            return Result.success(book);

        } catch (ValidationException e) {
            return Result.failure("Validation failed: " + e.getMessage());
        } catch (AuthorNotFoundException | GenreNotFoundException | BookAlreadyExistsException | DuplicateItemException e) {
            revertAddBook(book, inventory);
            return Result.failure(e.getMessage());
        }
    }

    private void revertAddBook(Book book, Inventory inventory) {
        if(Objects.nonNull(book)){
            this.bookService.remove(book);
            this.authorService.removeBook(book.getAuthorId(), book);
            this.genreService.removeBook(book.getGenreId(), book);
        }
        if(Objects.nonNull(inventory)){
            this.inventoryService.remove(inventory);
        }
    }

    public Result<Book> getBookById(UUID bookId) {
        try {
            Book book = this.bookService.getById(bookId);
            return Result.success(book);
        } catch (BookNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }
}
