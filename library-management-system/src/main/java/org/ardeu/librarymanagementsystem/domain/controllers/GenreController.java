package org.ardeu.librarymanagementsystem.domain.controllers;

import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreAlreadyExistsException;
import org.ardeu.librarymanagementsystem.domain.exceptions.genre.GenreNotFoundException;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.services.GenreService;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;
import org.ardeu.librarymanagementsystem.domain.validators.genre.GenreDTOValidator;

import java.util.UUID;

public class GenreController {
    private final GenreService genreService = ServiceRegistry.getInstance().getService(GenreService.class);
    private final Validator<GenreDTO> genreDTOValidator;

    public GenreController() {
        this.genreDTOValidator = new GenreDTOValidator();
    }

    public Result<Genre> getGenreById(UUID id){
        try {
            Genre genre = this.genreService.getById(id);
            return Result.success(genre);
        } catch (GenreNotFoundException e) {
            return Result.failure(e.getMessage());
        }
    }

    public Result<ObservableMap<UUID, Genre>> getAllGenres() {
        ObservableMap<UUID, Genre> genres = this.genreService.getItems();
        return Result.success(genres);
    }

    public Result<Genre> addGenre(GenreDTO genreDTO) {
        try {
            this.genreDTOValidator.validate(genreDTO);
            this.genreService.checkGenreExistsByName(genreDTO.name());
            Genre genre = this.genreService.create(genreDTO);
            this.genreService.add(genre);
            return Result.success(genre);
        } catch (ValidationException e) {
            return Result.failure("Validation failed: " + e.getMessage());
        } catch (GenreAlreadyExistsException | DuplicateItemException e) {
            return Result.failure(e.getMessage());
        }
    }
}
