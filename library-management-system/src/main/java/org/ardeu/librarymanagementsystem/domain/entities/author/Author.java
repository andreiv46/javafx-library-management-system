package org.ardeu.librarymanagementsystem.domain.entities.author;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;

import java.util.HashSet;
import java.util.UUID;

/**
 * Author represents an author of a book in the library management system.
 * <p>
 *     This class extends {@link BaseEntity} to inherit common entity properties.
 * </p>
 * @see BaseEntity
 */
public class Author extends BaseEntity {

    /**
     * The name of the author.
     */
    private String name;

    /**
     * A set of unique identifiers for the books associated with the author.
     */
    private HashSet<UUID> books;

    /**
     * Constructs a new {@code Author} with the specified unique identifier, name, and set of associated book identifiers.
     *
     * @param id    the unique identifier for the author
     * @param name  the name of the author
     * @param books the set of book identifiers (UUIDs) associated with the author
     */
    public Author(UUID id, String name, HashSet<UUID> books) {
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
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }

    /**
     * Adds a book to the set of books associated with the author.
     * <p>
     * This method takes a {@link Book} object and adds its unique identifier to
     * the author's list of books.
     * </p>
     *
     * @param book the {@link Book} to add to the author's collection
     */
    public void addBook(Book book) {
        this.books.add(book.getId());
    }
}
