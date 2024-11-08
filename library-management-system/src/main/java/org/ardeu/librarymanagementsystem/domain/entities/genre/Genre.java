package org.ardeu.librarymanagementsystem.domain.entities.genre;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;

import java.util.HashSet;
import java.util.UUID;

/**
 * Represents a genre in the library management system.
 * <p>
 *     This class extends {@link BaseEntity} to inherit common entity properties.
 * </p>
 * @see BaseEntity
 */
public class Genre extends BaseEntity {
    /**
     * The name of the genre.
     */
    private String name;

    /**
     * A set of unique identifiers for the books associated with the genre.
     */
    private HashSet<UUID> books;

    /**
     * Constructs a new Genre object with the specified unique identifier, name, and set of books.
     *
     * @param id the unique identifier for the genre
     * @param name the name of the genre
     * @param books the set of book identifiers associated with the genre
     */
    public Genre(UUID id, String name, HashSet<UUID> books) {
        super(id);
        this.name = name;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashSet<UUID> getBooks() {
        return books;
    }

    public void setBooks(HashSet<UUID> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }

    /**
     * Adds a book to the set of books associated with this genre.
     *
     * @param book the book to add to the genre
     */
    public void addBook(Book book) {
        this.books.add(book.getId());
    }


    /**
     * Removes a book from the set of books associated with this genre.
     *
     * @param book the book to remove from the genre
     */
    public void removeBook(Book book) {
        this.books.remove(book.getId());
    }
}
