package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.BookCreationDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Service class for managing {@link Book} entities, including creating, checking existence,
 * removing, and retrieving books.
 */
public class BookService extends Service<Book> {

    /**
     * Constructs a new {@link BookService} with the specified file handler.
     *
     * @param fileHandler the file handler to be used for saving and loading {@link Book} data
     */
    public BookService(MapFileHandler<UUID, Book> fileHandler) {
        super(fileHandler);
    }

    /**
     * Creates a new {@link Book} instance based on the provided {@link BookCreationDTO}.
     *
     * @param bookDTO the data transfer object containing book information
     * @return the newly created {@link Book}
     */
    public Book create(BookCreationDTO bookDTO) {
        return Book.builder()
                .setId(UUID.randomUUID())
                .setTitle(bookDTO.title())
                .setDescription(bookDTO.description())
                .setAuthorId(bookDTO.authorId())
                .setGenreId(bookDTO.genreId())
                .setPublishDate(bookDTO.releaseDate())
                .build();
    }

    /**
     * Checks if the specified {@link Book} already exists in the system.
     *
     * @param book the book to check
     * @throws BookAlreadyExistsException if a book with the same details already exists
     */
    public void checkBookExists(Book book) throws BookAlreadyExistsException {
        for(Book b : super.items.values()){
            if(b.equals(book)){
                throw new BookAlreadyExistsException("Book already exists");
            }
        }
    }

    /**
     * Removes the specified {@link Book} from the system.
     *
     * @param book the book to remove
     */
    public void remove(Book book) {
        if(Objects.nonNull(book)) {
            super.items.remove(book.getId());
        }
    }

    /**
     * Retrieves a {@link Book} by its ID.
     *
     * @param bookId the ID of the book to retrieve
     * @return the {@link Book} with the specified ID
     * @throws BookNotFoundException if no book with the specified ID is found
     */
    public Book getById(UUID bookId) throws BookNotFoundException {
        Book book = super.items.get(bookId);
        if(book == null){
            throw new BookNotFoundException("Book with id " + bookId + " not found");
        }
        return book;
    }
}
