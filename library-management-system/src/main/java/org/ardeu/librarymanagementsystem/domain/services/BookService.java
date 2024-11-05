package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.BookDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.book.BookNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.Objects;
import java.util.UUID;

public class BookService extends Service<Book> {

    public BookService(MapFileHandler<UUID, Book> fileHandler) {
        super(fileHandler);
    }

    public Book create(BookDTO bookDTO) {
        return Book.builder()
                .setId(UUID.randomUUID())
                .setTitle(bookDTO.title())
                .setDescription(bookDTO.description())
                .setAuthorId(bookDTO.authorId())
                .setGenreId(bookDTO.genreId())
                .setPublishDate(bookDTO.releaseDate())
                .build();
    }

    public void checkBookExists(Book book) throws BookAlreadyExistsException {
        for(Book b : super.items.values()){
            if(b.equals(book)){
                throw new BookAlreadyExistsException("Book already exists");
            }
        }
    }

    public void remove(Book book) {
        if(Objects.nonNull(book)) {
            super.items.remove(book.getId());
        }
    }

    public Book getById(UUID bookId) throws BookNotFoundException {
        Book book = super.items.get(bookId);
        if(book == null){
            throw new BookNotFoundException("Book with id " + bookId + " not found");
        }
        return book;
    }
}
