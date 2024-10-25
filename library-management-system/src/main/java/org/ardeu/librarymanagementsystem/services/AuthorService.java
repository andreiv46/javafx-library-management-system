package org.ardeu.librarymanagementsystem.services;

import org.ardeu.librarymanagementsystem.entities.author.Author;
import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;
import org.ardeu.librarymanagementsystem.services.base.Service;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class AuthorService extends Service<Author> {

    private static final Logger logger = Logger.getLogger(AuthorService.class.getName());

    public AuthorService(FileHandler<Author> fileHandler) {
        super(fileHandler);
        logger.info("Initialized author service");
    }

    public void addAuthor(Author author){
        super.items.putIfAbsent(author.getId(), author);
    }

    public void removeAuthor(UUID id){
        super.items.remove(id);
    }

    public void addBook(UUID authorId, Book book) {
        Author author = super.items.get(authorId);
        if(Objects.nonNull(author)){
            author.addBook(book);
        }
    }
}
