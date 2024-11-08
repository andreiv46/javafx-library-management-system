package org.ardeu.librarymanagementsystem.domain.services.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code Service} class provides generic functionality for managing items
 * of type {@code T} (which extends {@code BaseEntity}). It includes methods for
 * saving and loading the items to/from persistent storage, as well as adding new items
 * and managing them in an observable map.
 *
 * @param <T> the type of the items, extending {@code BaseEntity}
 */
public class Service<T extends BaseEntity> implements DataService {

    /**
     * The observable map containing the items of type {@code T}, indexed by their {@code UUID}.
     */
    protected ObservableMap<UUID, T> items;

    /**
     * The file handler responsible for reading from and writing to persistent storage.
     */
    private final MapFileHandler<UUID, T> fileHandler;

    /**
     * Constructs a new {@code Service} with the given file handler.
     * The file handler is used to read from and write to persistent storage.
     *
     * @param fileHandler the file handler used to manage the storage of the items
     */
    public Service(MapFileHandler<UUID, T> fileHandler) {
        this.fileHandler = fileHandler;
        this.items = FXCollections.observableHashMap();
    }

    /**
     * Saves the current items to persistent storage using the {@code fileHandler}.
     * The {@code items} map is serialized and written to the storage medium.
     *
     * @throws IOException if an I/O error occurs while saving data
     */
    @Override
    public void save() throws IOException {
        this.fileHandler.writeToFile(this.items);
    }

    /**
     * Loads the items from persistent storage using the {@code fileHandler}.
     * The current {@code items} map is cleared and populated with the loaded data.
     *
     * @throws IOException if an I/O error occurs while loading data
     */
    @Override
    public void load() throws IOException {
        this.items.clear();
        this.items.putAll(this.fileHandler.readFromFile());
    }

    /**
     * Gets the observable map of items.
     *
     * @return the observable map of items
     */
    public ObservableMap<UUID, T> getItems() {
        return items;
    }

    /**
     * Adds a new item to the {@code items} map. If an item with the same ID already exists,
     * a {@code DuplicateItemException} is thrown.
     *
     * @param item the item to add
     * @throws DuplicateItemException if an item with the same ID already exists in the map
     */
    public void add(T item) throws DuplicateItemException {
        if (Objects.nonNull(this.items.get(item.getId()))) {
            throw new DuplicateItemException("Item with id " + item.getId() + " already exists");
        } else {
            this.items.put(item.getId(), item);
        }
    }
}
