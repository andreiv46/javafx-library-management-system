package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.author.Author;
import org.ardeu.librarymanagementsystem.fileio.baseio.FileHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AuthorService implements DataService{
    private Map<UUID, Author> authors;
    private final FileHandler<Author> fileHandler;

    public AuthorService(FileHandler<Author> fileHandler) {
        this.fileHandler = fileHandler;
        this.authors = new HashMap<>();
    }

    @Override
    public void save() throws IOException {
        this.fileHandler.writeToFile(authors.values().stream().toList());
    }

    @Override
    public void load() throws IOException {
        this.authors = this.fileHandler
                .readFromFile()
                .stream()
                .collect(Collectors.toMap(Author::getId, Function.identity()));
    }

    public Map<UUID, Author> getAuthors() {
        return authors;
    }
}
