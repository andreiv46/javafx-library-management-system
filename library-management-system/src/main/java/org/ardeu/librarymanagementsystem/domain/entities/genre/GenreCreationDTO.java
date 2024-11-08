package org.ardeu.librarymanagementsystem.domain.entities.genre;

/**
 * A Data Transfer Object (DTO) used to create a new {@link Genre} object.
 * <p>
 * This class only contains the name field, which is required for basic creation
 * of an {@code Genre}. Other fields of the {@code Genre} class are set separately
 * after creation if needed.
 * </p>
 * @param name The name of the genre.
 * @see Genre
 */
public record GenreCreationDTO(String name) { }
