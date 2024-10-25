package org.ardeu.librarymanagementsystem.entities.base;

import java.io.Serializable;
import java.util.UUID;

public class BaseEntity implements Serializable {
    protected UUID id;

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
