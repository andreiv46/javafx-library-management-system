package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;
import org.ardeu.librarymanagementsystem.services.base.Service;

import java.util.Objects;
import java.util.UUID;

public class GenreService extends Service<Genre> {
    public GenreService(FileHandler<Genre> fileHandler) {
        super(fileHandler);
    }

    public void addBook(UUID genreId, Book book) {
        Genre genre = super.items.get(genreId);
        if(Objects.nonNull(genre)){
            genre.addBook(book);
        }
    }
}
