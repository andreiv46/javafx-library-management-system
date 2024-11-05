package org.ardeu.librarymanagementsystem.domain.entities.inventory;

import java.util.UUID;

public record InventoryDTO(UUID bookId, int totalCopies, int availableCopies, double price) { }
