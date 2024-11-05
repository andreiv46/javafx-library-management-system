package org.ardeu.librarymanagementsystem.domain.entities.inventory;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;

import java.util.UUID;

public class Inventory extends BaseEntity {
    private UUID bookid;
    private int availableCopies;
    private int totalCopies;
    private double price;

    public Inventory(UUID id, UUID bookid, int availableCopies, int totalCopies, double price) {
        super(id);
        this.bookid = bookid;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.price = price;
    }

    public UUID getBookid() {
        return bookid;
    }

    public void setBookid(UUID bookid) {
        this.bookid = bookid;
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
                ", bookid=" + bookid +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                ", price=" + price +
                '}';
    }
}
