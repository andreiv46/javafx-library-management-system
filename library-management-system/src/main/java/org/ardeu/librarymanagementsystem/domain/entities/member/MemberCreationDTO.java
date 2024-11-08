package org.ardeu.librarymanagementsystem.domain.entities.member;

/**
 * A Data Transfer Object (DTO) used to create a new {@link Member} object.
 * <p>
 *     This class contains the fields required to create a new {@code Member} object.
 *     The fields are the name of the author.
 *     Other fields of the {@code Member} class are set separately after creation if needed.
 * </p>
 * @param name The name of the author.
 * @see Member
 */
public record MemberCreationDTO(String name, String email) { }
