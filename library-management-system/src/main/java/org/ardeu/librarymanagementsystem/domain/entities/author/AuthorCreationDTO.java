package org.ardeu.librarymanagementsystem.domain.entities.author;

/**
 * A Data Transfer Object (DTO) used to create a new {@link Author} object.
 * <p>
 *     This class contains the fields required to create a new {@code Author} object.
 *     The fields are the name of the author.
 *     Other fields of the {@code Author} class are set separately after creation if needed.
 * </p>
 * @param name The name of the author.
 * @see Author
 */
public record AuthorCreationDTO(String name) { }
