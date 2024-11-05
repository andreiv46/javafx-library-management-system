package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class GenreService extends Service<Genre> {

    public GenreService(MapFileHandler<UUID, Genre> fileHandler) {
        super(fileHandler);
    }

    public void addBook(UUID genreId, Book book) throws GenreNotFoundException {
        Genre genre = super.items.get(genreId);
        if(Objects.isNull(genre)){
            throw new GenreNotFoundException("Genre with id " + genreId + " not found");
        }
        genre.addBook(book);
    }

    public Genre getById(UUID id) throws GenreNotFoundException {
        Genre genre = super.items.get(id);
        if (Objects.isNull(genre)) {
            throw new GenreNotFoundException("Genre with id " + id + " not found");
        }
        return genre;
    }

    public void checkGenreExistsByName(String name) throws GenreAlreadyExistsException {
        for (Genre genre : super.items.values()) {
            if (genre.getName().equals(name)) {
                throw new GenreAlreadyExistsException("Genre with name " + name + " already exists");
            }
        }
    }

    public Genre create(GenreDTO genreDTO) {
        return new Genre(UUID.randomUUID(), genreDTO.name(), new HashSet<>());
    }


    public void removeBook(UUID genreId, Book book) {
        Genre genre = super.items.get(genreId);
        if(Objects.nonNull(book)) {
            genre.removeBook(book);
        }
    }
}
