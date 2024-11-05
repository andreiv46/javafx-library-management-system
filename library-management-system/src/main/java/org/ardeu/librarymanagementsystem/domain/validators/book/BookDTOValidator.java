package org.ardeu.librarymanagementsystem.domain.validators.book;

import org.ardeu.librarymanagementsystem.domain.entities.book.BookDTO;
import org.ardeu.librarymanagementsystem.domain.exceptions.validation.ValidationException;
import org.ardeu.librarymanagementsystem.domain.validators.base.Validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BookDTOValidator implements Validator<BookDTO> {
    @Override
    public void validate(BookDTO bookDTO) throws ValidationException {
        List<String> errors = new ArrayList<>();
        validateTitle(bookDTO.title(), errors);
        validateDescription(bookDTO.description(), errors);
        validateAuthorId(bookDTO.authorId(), errors);
        validateGenreId(bookDTO.genreId(), errors);
        validatePublicationDate(bookDTO.releaseDate(), errors);

        if(!errors.isEmpty()){
            throw new ValidationException(String.join("\n", errors));
        }
    }

    private void validateTitle(String title, List<String> errors){
        if(Objects.isNull(title)){
            errors.add("Title cannot be null");
        }
        if(title.isBlank()){
            errors.add("Title cannot be empty");
        }
    }

    private void validateDescription(String description, List<String> errors){
        if(Objects.isNull(description)){
            errors.add("Description cannot be null");
        }
        if(description.isBlank()){
            errors.add("Description cannot be empty");
        }
    }

    private void validateAuthorId(UUID authorId, List<String> errors){
        if(Objects.isNull(authorId)){
            errors.add("Author ID cannot be null");
        }
    }

    private void validateGenreId(UUID genreId, List<String> errors){
        if(Objects.isNull(genreId)){
            errors.add("Genre ID cannot be null");
        }
    }

    private void validatePublicationDate(LocalDate localDate, List<String> errors) {
        if(Objects.isNull(localDate)){
            errors.add("Publication date cannot be null");
        }
        if(localDate.isAfter(LocalDate.now())){
            errors.add("Publication date cannot be in the future");
        }
    }
}
