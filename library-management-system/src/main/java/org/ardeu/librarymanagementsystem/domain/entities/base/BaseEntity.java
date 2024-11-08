package org.ardeu.librarymanagementsystem.domain.entities.base;

import java.io.Serializable;
import java.util.UUID;

/**
 * BaseEntity is a base class for all entities in the library management system.
 * It provides a unique identifier for each entity.
 * <p>
 *     This class inherits from {@link Serializable} to allow serialization of entities.
 * </p>
 * @see Serializable
 */
public class BaseEntity implements Serializable {

    /**
     * The unique identifier of the entity.
     */
    protected UUID id;

    /**
     * Creates a new BaseEntity with the given identifier.
     *
     * @param id The unique identifier of the entity.
     */
    public BaseEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
