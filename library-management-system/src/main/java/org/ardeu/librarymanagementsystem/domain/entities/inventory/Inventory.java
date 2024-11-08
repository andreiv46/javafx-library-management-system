package org.ardeu.librarymanagementsystem.domain.entities.inventory;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;

import java.util.UUID;


/**
 * Represents an inventory record for a book in the library management system.
 * <p>
 *     This class extends {@link BaseEntity} to inherit common entity properties.
 * </p>
 * @see BaseEntity
 */
public class Inventory extends BaseEntity {
    /**
     * The unique identifier of the book associated with this inventory entry.
     */
    private UUID bookId;

    /**
     * The number of available copies of the book that can be borrowed.
     */
    private int availableCopies;

    /**
     * The total number of copies of the book in the library's inventory.
     */
    private int totalCopies;

    /**
     * The price of the book.
     */
    private double price;

    /**
     * Constructs an Inventory entry with the specified details.
     *
     * @param id the unique identifier of the inventory entry
     * @param bookId the unique identifier of the book
     * @param availableCopies the number of available copies for borrowing
     * @param totalCopies the total number of copies in the library's inventory
     * @param price the price of the book
     */
    public Inventory(UUID id, UUID bookId, int availableCopies, int totalCopies, double price) {
        super(id);
        this.bookId = bookId;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.price = price;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", bookid=" + bookId +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                ", price=" + price +
                '}';
    }
}
