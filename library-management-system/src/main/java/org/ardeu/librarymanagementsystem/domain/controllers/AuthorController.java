package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.author.AuthorNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.AuthorService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.author.AuthorDTOValidator;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.util.UUID;

public class AuthorController {
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private final Validator<AuthorDTO> authorDTOValidator;

    public AuthorController() {
        authorDTOValidator = new AuthorDTOValidator();
    }

    public Result<ObservableMap<UUID, Author>> getAllAuthors(){
        ObservableMap<UUID, Author> authors = this.authorService.getItems();
        return Result.success(authors);
    }

    public Result<Author> getAuthorById(UUID id){
        try {
            Author author = this.authorService.getById(id);
            return Result.success(author);
        } catch (AuthorNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<Author> addAuthor(AuthorDTO authorDTO) {
        try {
            this.authorDTOValidator.validate(authorDTO);
            this.authorService.authorExistsByName(authorDTO.name());
            Author author = this.authorService.create(authorDTO);
            this.authorService.add(author);
            return Result.success(author);
        } catch (ValidationException e) {
            return Result.failure("Validation failed: \n" + e.getMessage());
        } catch (AuthorAlreadyExistsException | DuplicateItemException e) {
            return Result.failure(e.getMessage());
        }
    }
}
