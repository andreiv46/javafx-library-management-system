package org.ardeu.librarymanagementsystem.domain.services.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class Service<T extends BaseEntity> implements DataService {

    protected ObservableMap<UUID, T> items;
    private final MapFileHandler<UUID, T> fileHandler;

    public Service(MapFileHandler<UUID, T> fileHandler) {
        this.fileHandler = fileHandler;
        this.items = FXCollections.observableHashMap();
    }

    @Override
    public void save() throws IOException {
        this.fileHandler.writeToFile(this.items);
    }

    @Override
    public void load() throws IOException {
        this.items.clear();
        this.items.putAll(this.fileHandler.readFromFile());
    }

    public ObservableMap<UUID, T> getItems() {
        return items;
    }

    public void add(T item) throws DuplicateItemException {
        if(Objects.nonNull(this.items.get(item.getId()))){
            throw new DuplicateItemException("Item with id " + item.getId() + " already exists");
        }
        else{
            this.items.put(item.getId(), item);
        }
    }
}
