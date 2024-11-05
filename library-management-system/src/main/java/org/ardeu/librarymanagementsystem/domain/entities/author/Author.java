package org.ardeu.librarymanagementsystem.domain.entities.author;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class Author extends BaseEntity {
    private String name;
    private HashSet<UUID> books;

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

    public void addBook(Book book) {
        this.books.add(book.getId());
    }

    public void removeBook(Book book) {
        this.books.remove(book.getId());
    }
}
