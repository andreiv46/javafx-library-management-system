package org.ardeu.librarymanagementsystem.entities.author;

import java.io.Serializable;
import java.util.HashSet;
import java.util.UUID;

public class Author implements Serializable {
    private UUID id;
    private String name;
    private HashSet<UUID> books;

    public Author(UUID id, String name, HashSet<UUID> books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
