package org.ardeu.librarymanagementsystem.domain.services;

import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorDTO;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorNotFoundException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;
import org.ardeu.librarymanagementsystem.domain.services.base.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class AuthorService extends Service<Author> {

    public AuthorService(MapFileHandler<UUID, Author> fileHandler) {
        super(fileHandler);
    }

    public void addBook(UUID authorId, Book book) throws AuthorNotFoundException {
        Author author = super.items.get(authorId);
        if(Objects.isNull(author)){
            throw new AuthorNotFoundException("Author with id: " + authorId + " not found");
        }
        author.addBook(book);
    }

    public Author getById(UUID id) throws AuthorNotFoundException {
        Author author = super.items.get(id);
        if(Objects.isNull(author)){
            throw new AuthorNotFoundException("Author with id: " + id + " not found");
        }
        return author;
    }

    public Author create(AuthorDTO authorDTO) {
        return new Author(UUID.randomUUID(), authorDTO.name(), new HashSet<>());
    }

    public void authorExistsByName(String name) throws AuthorAlreadyExistsException{
        for(Author author : super.items.values()){
            if(author.getName().equals(name)){
                throw new AuthorAlreadyExistsException("Author with name: " + name + " already exists");
            }
        }
    }

    public void removeBook(UUID authorId, Book book) {
        Author author = super.items.get(authorId);
        if(Objects.nonNull(book)) {
            author.removeBook(book);
        }
    }
}
