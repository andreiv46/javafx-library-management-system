package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.InventoryCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.InventoryNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.inventory.NoAvailableCopiesException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Service class for managing {@link Inventory} entities, including borrowing and returning books,
 * creating inventories, and retrieving inventory details for books.
 */
public class InventoryService extends Service<Inventory> {

    /**
     * Constructs a new {@link InventoryService} with the specified file handler.
     *
     * @param fileHandler the file handler to be used for saving and loading {@link Inventory} data
     */
    public InventoryService(MapFileHandler<UUID, Inventory> fileHandler) {
        super(fileHandler);
    }

    /**
     * Retrieves the {@link Inventory} for the specified book ID.
     *
     * @param bookId the ID of the book whose inventory will be retrieved
     * @return the {@link Inventory} for the specified book
     * @throws InventoryNotFoundException if no inventory exists for the book
     */
    public Inventory getByBookId(UUID bookId) throws InventoryNotFoundException {
        for (Inventory inventory : items.values()) {
            if (inventory.getBookId().equals(bookId)) {
                return inventory;
            }
        }
        throw new InventoryNotFoundException("Inventory for book with ID " + bookId + " not found");
    }

    /**
     * Creates a new {@link Inventory} instance based on the provided {@link InventoryCreationDTO}.
     *
     * @param inventoryDTO the data transfer object containing inventory information
     * @return the newly created {@link Inventory}
     */
    public Inventory create(InventoryCreationDTO inventoryDTO) {
        return new Inventory(
                UUID.randomUUID(),
                inventoryDTO.bookId(),
                inventoryDTO.totalCopies(),
                inventoryDTO.availableCopies(),
                inventoryDTO.price()
        );
    }

    /**
     * Removes a {@link Inventory} from the system.
     *
     * @param inventory the {@link Inventory} object to remove
     */
    public void remove(Inventory inventory) {
        super.items.remove(inventory.getId());
    }

    /**
     * Filters a list of books to return only those with available copies in the inventory.
     *
     * @param books the list of books to filter
     * @return a list of books that have available copies in the inventory
     */
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

    /**
     * Borrows a book by decreasing its available copies by 1.
     *
     * @param bookId the ID of the book to borrow
     * @throws InventoryNotFoundException if no inventory exists for the book
     * @throws NoAvailableCopiesException if there are no available copies of the book
     */
    public void borrowBook(UUID bookId) throws InventoryNotFoundException, NoAvailableCopiesException {
        Inventory inventory = items.values().stream()
                .filter(i -> i.getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new InventoryNotFoundException("Inventory for book with ID " + bookId + " not found"));

        if (inventory.getAvailableCopies() == 0) {
            throw new NoAvailableCopiesException("No available copies for book with ID " + bookId);
        }

        inventory.setAvailableCopies(inventory.getAvailableCopies() - 1);
        items.put(inventory.getId(), inventory);
    }

    /**
     * Reverts the inventory update by increasing the available copies of the book by 1.
     *
     * @param bookId the ID of the book for which the inventory will be updated
     */
    public void revertInventoryUpdate(UUID bookId) {
        if (Objects.isNull(bookId)) {
            return;
        }
        for (Inventory inventory : items.values()) {
            if (inventory.getBookId().equals(bookId)) {
                inventory.setAvailableCopies(inventory.getAvailableCopies() + 1);
                items.put(inventory.getId(), inventory);
                break;
            }
        }
    }

    /**
     * Returns a book by increasing its available copies by 1.
     *
     * @param bookId the ID of the book being returned
     * @throws InventoryNotFoundException if no inventory exists for the book
     */
    public void returnBook(UUID bookId) throws InventoryNotFoundException {
        Inventory inventory = items.values().stream()
                .filter(i -> i.getBookId().equals(bookId))
                .findFirst()
                .orElseThrow(() -> new InventoryNotFoundException("Inventory for book with ID " + bookId + " not found"));
        inventory.setAvailableCopies(inventory.getAvailableCopies() + 1);
        items.put(inventory.getId(), inventory);
    }
}

