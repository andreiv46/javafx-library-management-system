package org.ardeu.librarymanagementsystem.domain.entities.book;

import java.time.LocalDate;
import java.util.UUID;

public record BookDTO(
        String title,
        String description,
        UUID authorId,
        UUID genreId,
        LocalDate releaseDate
) { }
