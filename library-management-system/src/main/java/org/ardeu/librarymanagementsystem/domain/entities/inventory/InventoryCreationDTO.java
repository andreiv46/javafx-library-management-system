package org.ardeu.librarymanagementsystem.domain.entities.inventory;

import org.ardeu.librarymanagementsystem.domain.entities.author.Author;

import java.util.UUID;

/**
 * A Data Transfer Object (DTO) used to create a new {@link Inventory} object.
 * <p>
 * This class only contains the name field, which is required for basic creation
 * of an {@code Inventory}. Other fields of the {@code Inventory} class are set separately
 * after creation if needed.
 * </p>
 * @see Inventory
 */
public record InventoryCreationDTO(
        UUID bookId,
        int totalCopies,
        int availableCopies,
        double price
) { }
