package org.ardeu.librarymanagementsystem.domain.entities.book;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Data Transfer Object for creating a new Book.
 * <p>
 *     This class contains the fields required to create a new {@link Book} object.
 *     The fields are the title, description, authorId, genreId, and releaseDate.
 *     Other fields of the {@code Book} class are set separately after creation if needed.
 *</p>
 * @param title The title of the book.
 * @param description A description of the book's content.
 * @param authorId The unique identifier of the book's author.
 * @param genreId The unique identifier of the book's genre.
 * @param releaseDate The release date of the book.
 * @see Book
 */
public record BookCreationDTO(
        String title,
        String description,
        UUID authorId,
        UUID genreId,
        LocalDate releaseDate
) { }
