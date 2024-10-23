package org.ardeu.librarymanagementsystem.entities.inventory;

import java.io.Serializable;
import java.util.UUID;

public class Inventory implements Serializable {
    private UUID bookid;
    private int availableCopies;
    private int totalCopies;
    private double price;

    public Inventory(UUID bookid, int availableCopies, int totalCopies, double price) {
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
                "bookid=" + bookid +
                ", availableCopies=" + availableCopies +
                ", totalCopies=" + totalCopies +
                ", price=" + price +
                '}';
    }
}
