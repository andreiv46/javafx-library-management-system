package org.ardeu.librarymanagementsystem.services.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.ardeu.librarymanagementsystem.entities.base.BaseEntity;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Service<T extends BaseEntity> implements DataService {

    protected ObservableMap<UUID, T> items;
    private final FileHandler<T> fileHandler;

    public Service(FileHandler<T> fileHandler) {
        this.fileHandler = fileHandler;
        this.items = FXCollections.observableHashMap();
    }

    @Override
    public void save() throws IOException {
        this.fileHandler.writeToFile(items.values().stream().toList());
    }

    @Override
    public void load() throws IOException {
        List<T> itemsList = this.fileHandler.readFromFile();
        itemsList.forEach(item -> this.items.put(item.getId(), item));
    }

    public ObservableMap<UUID, T> getItems() {
        return items;
    }

    public void add(T item) throws Exception {
        if(Objects.nonNull(this.items.get(item.getId()))){
            throw new Exception("item already exists");
        }
        else{
            this.items.put(item.getId(), item);
        }
    }
}
