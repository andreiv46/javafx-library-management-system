package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.BookCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.book.mappers.BookExportField;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.InventoryCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.InventoryNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.*;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.book.BookDTOValidator;
import org.ardeu.librarymanagementsystem.domain.validators.inventory.InventoryDTOValidator;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * BookController is responsible for handling author-related operations.
 */
public class BookController {
    private final BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private final GenreService genreService = ServiceRegistry.getInstance().getService(GenreService.class);
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);
    private final LoanService loanService = ServiceRegistry.getInstance().getService(LoanService.class);
    private final Validator<BookCreationDTO> bookValidator = new BookDTOValidator();
    private final Validator<InventoryCreationDTO> inventoryValidator = new InventoryDTOValidator();

    /**
     * Retrieves all books from the system.
     *
     * @return a Result containing an ObservableMap of books, or a failure message if an error occurs
     */
    public Result<ObservableMap<UUID, Book>> getAllBooks(){
        ObservableMap<UUID, Book> books = this.bookService.getItems();
        return Result.success(books);
    }

    /**
     * Adds a new book to the system.
     *
     * @param bookDTO the data transfer object containing book creation details
     * @param totalCopies the total number of copies of the book
     * @param price the price of the book
     * @return a Result containing the created Book, or a failure message if validation fails or the book already exists
     */
    public Result<Book> addBook(BookCreationDTO bookDTO, int totalCopies, double price) {
        Book book = null;
        Inventory inventory = null;
        try {
            this.bookValidator.validate(bookDTO);
            book = this.bookService.create(bookDTO);

            InventoryCreationDTO inventoryDTO = new InventoryCreationDTO(book.getId(), totalCopies, totalCopies, price);
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

    /**
     * Reverts the addition of a book and its inventory in case of an error.
     *
     * @param book the book to revert
     * @param inventory the inventory to revert
     */
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

    /**
     * Retrieves a book by its ID.
     *
     * @param bookId the UUID of the book
     * @return a Result containing the Book object if found, or a failure message if not found
     */
    public Result<Book> getBookById(UUID bookId) {
        try {
            Book book = this.bookService.getById(bookId);
            return Result.success(book);
        } catch (BookNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * Exports the book data to a CSV file with a specified set of fields.
     *
     * @param file the file to export to
     * @param bookList the list of books to export
     * @param checkModel the fields to include in the export
     * @return a Result indicating success or failure of the export operation
     */
    public Result<Void> exportBooksToCSV(File file, List<Book> bookList, List<BookExportField> checkModel) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(checkModel.stream().map(BookExportField::getDisplayName).toArray(String[]::new))
                .build();
        try(CSVPrinter printer =
                    new CSVPrinter(
                            new BufferedWriter(
                                    new OutputStreamWriter(
                                            new FileOutputStream(file))), csvFormat)){
            for (Book book : bookList) {
                Author author = this.authorService.getById(book.getAuthorId());
                Genre genre = this.genreService.getById(book.getGenreId());
                Inventory inventory = this.inventoryService.getByBookId(book.getId());
                printer.printRecord(
                        checkModel.stream().map(field -> switch (field) {
                            case ID -> book.getId();
                            case TITLE -> book.getTitle();
                            case AUTHOR_ID -> author.getId();
                            case AUTHOR_NAME -> author.getName();
                            case PUBLICATION_DATE -> book.getPublishDate();
                            case GENRE_ID -> genre.getId();
                            case GENRE_NAME -> genre.getName();
                            case BOOKS_AVAILABLE -> inventory.getAvailableCopies();
                            case BOOKS_STOCK -> inventory.getTotalCopies();
                            case BOOKS_PRICE -> inventory.getPrice();
                            case TOTAL_LOANS -> this.loanService.getTotalLoansByBookId(book.getId());
                        }).toArray());
            }
            return Result.success();
        } catch (IOException e) {
            return Result.failure("Error exporting books to CSV");
        } catch (AuthorNotFoundException | GenreNotFoundException | InventoryNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }
}
