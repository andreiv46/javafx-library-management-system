package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * A service class responsible for managing {@link Author} entities.
 * Provides methods for adding, removing, and retrieving authors and their associated books.
 */
public class AuthorService extends Service<Author> {

    /**
     * Constructs an {@link AuthorService} with the specified file handler.
     *
     * @param fileHandler the file handler used for reading and writing author data
     */
    public AuthorService(MapFileHandler<UUID, Author> fileHandler) {
        super(fileHandler);
    }

    /**
     * Adds a book to the author's list of books.
     * Throws an {@link AuthorNotFoundException} if the author with the given ID does not exist.
     *
     * @param authorId the ID of the author to add the book to
     * @param book the book to be added to the author
     * @throws AuthorNotFoundException if no author with the given ID exists
     */
    public void addBook(UUID authorId, Book book) throws AuthorNotFoundException {
        Author author = super.items.get(authorId);
        if (Objects.isNull(author)) {
            throw new AuthorNotFoundException("Author with id: " + authorId + " not found");
        }
        author.addBook(book);
        super.items.put(authorId, author);
    }

    /**
     * Retrieves an author by their ID.
     * Throws an {@link AuthorNotFoundException} if no author with the given ID exists.
     *
     * @param id the ID of the author to retrieve
     * @return the author with the given ID
     * @throws AuthorNotFoundException if no author with the given ID exists
     */
    public Author getById(UUID id) throws AuthorNotFoundException {
        Author author = super.items.get(id);
        if (Objects.isNull(author)) {
            throw new AuthorNotFoundException("Author with id: " + id + " not found");
        }
        return author;
    }

    /**
     * Creates a new author based on the provided {@link AuthorCreationDTO}.
     *
     * @param authorCreationDTO the DTO containing the author information
     * @return the newly created author
     */
    public Author create(AuthorCreationDTO authorCreationDTO) {
        return new Author(UUID.randomUUID(), authorCreationDTO.name(), new HashSet<>());
    }

    /**
     * Checks if an author with the given name already exists.
     * Throws an {@link AuthorAlreadyExistsException} if an author with the same name is found.
     *
     * @param name the name of the author to check for existence
     * @throws AuthorAlreadyExistsException if an author with the given name already exists
     */
    public void authorExistsByName(String name) throws AuthorAlreadyExistsException {
        for (Author author : super.items.values()) {
            if (author.getName().equals(name)) {
                throw new AuthorAlreadyExistsException("Author with name: " + name + " already exists");
            }
        }
    }

    /**
     * Removes a book from the author's list of books.
     * If the book is present, it will be removed from the author's list.
     *
     * @param authorId the ID of the author from whom to remove the book
     * @param book the book to remove from the author's list
     */
    public void removeBook(UUID authorId, Book book) {
        Author author = super.items.get(authorId);
        if (Objects.nonNull(book)) {
            super.items.put(authorId, author);
        }
    }

    /**
     * Returns the count of books associated with an author.
     * Throws an {@link AuthorNotFoundException} if no author with the given ID exists.
     *
     * @param id the ID of the author to get the book count for
     * @return the number of books associated with the author
     * @throws AuthorNotFoundException if no author with the given ID exists
     */
    public double getBooksCount(UUID id) throws AuthorNotFoundException {
        Author author = getById(id);
        return author.getBooks().size();
    }

    /**
     * Returns the set of book IDs associated with an author.
     * Throws an {@link AuthorNotFoundException} if no author with the given ID exists.
     *
     * @param id the ID of the author to get the book IDs for
     * @return the set of book IDs associated with the author
     * @throws AuthorNotFoundException if no author with the given ID exists
     */
    public Set<UUID> getBookIds(UUID id) throws AuthorNotFoundException {
        Author author = getById(id);
        return author.getBooks();
    }
}
