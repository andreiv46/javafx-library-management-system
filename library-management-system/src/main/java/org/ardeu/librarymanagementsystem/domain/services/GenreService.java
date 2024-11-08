package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Service class for managing {@link Genre} entities, including adding/removing books,
 * creating genres, and retrieving genre details.
 */
public class GenreService extends Service<Genre> {

    /**
     * Constructs a new {@link GenreService} with the specified file handler.
     *
     * @param fileHandler the file handler to be used for saving and loading {@link Genre} data
     */
    public GenreService(MapFileHandler<UUID, Genre> fileHandler) {
        super(fileHandler);
    }

    /**
     * Adds a {@link Book} to the specified genre.
     *
     * @param genreId the ID of the genre to which the book will be added
     * @param book the book to add to the genre
     * @throws GenreNotFoundException if the genre with the specified ID is not found
     */
    public void addBook(UUID genreId, Book book) throws GenreNotFoundException {
        Genre genre = super.items.get(genreId);
        if(Objects.isNull(genre)){
            throw new GenreNotFoundException("Genre with id " + genreId + " not found");
        }
        genre.addBook(book);
        super.items.put(genreId, genre);
    }

    /**
     * Retrieves a {@link Genre} by its ID.
     *
     * @param id the ID of the genre to retrieve
     * @return the {@link Genre} with the specified ID
     * @throws GenreNotFoundException if the genre with the specified ID is not found
     */
    public Genre getById(UUID id) throws GenreNotFoundException {
        Genre genre = super.items.get(id);
        if (Objects.isNull(genre)) {
            throw new GenreNotFoundException("Genre with id " + id + " not found");
        }
        return genre;
    }

    /**
     * Checks if a genre with the specified name already exists in the system.
     *
     * @param name the name of the genre to check
     * @throws GenreAlreadyExistsException if a genre with the specified name already exists
     */
    public void checkGenreExistsByName(String name) throws GenreAlreadyExistsException {
        for (Genre genre : super.items.values()) {
            if (genre.getName().equals(name)) {
                throw new GenreAlreadyExistsException("Genre with name " + name + " already exists");
            }
        }
    }

    /**
     * Creates a new {@link Genre} instance based on the provided {@link GenreCreationDTO}.
     *
     * @param genreDTO the data transfer object containing genre information
     * @return the newly created {@link Genre}
     */
    public Genre create(GenreCreationDTO genreDTO) {
        return new Genre(UUID.randomUUID(), genreDTO.name(), new HashSet<>());
    }

    /**
     * Removes a {@link Book} from the specified genre.
     *
     * @param genreId the ID of the genre from which the book will be removed
     * @param book the book to remove from the genre
     */
    public void removeBook(UUID genreId, Book book) {
        Genre genre = super.items.get(genreId);
        if(Objects.nonNull(book)) {
            genre.removeBook(book);
            super.items.put(genreId, genre);
        }
    }

    /**
     * Retrieves the count of books in the specified genre.
     *
     * @param id the ID of the genre whose book count will be retrieved
     * @return the count of books in the genre
     * @throws GenreNotFoundException if the genre with the specified ID is not found
     */
    public double getBooksCount(UUID id) throws GenreNotFoundException {
        Genre genre = getById(id);
        return genre.getBooks().size();
    }

    /**
     * Retrieves the IDs of the books associated with the specified genre.
     *
     * @param genreId the ID of the genre whose book IDs will be retrieved
     * @return the set of book IDs associated with the genre
     * @throws GenreNotFoundException if the genre with the specified ID is not found
     */
    public Set<UUID> getBookIds(UUID genreId) throws GenreNotFoundException {
        Genre genre = getById(genreId);
        return genre.getBooks();
    }
}

