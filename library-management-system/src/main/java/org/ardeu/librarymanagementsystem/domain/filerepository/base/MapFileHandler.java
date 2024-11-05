package org.ardeu.librarymanagementsystem.domain.filerepository.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class MapFileHandler<K, V> {
    private final String fileName;

    public MapFileHandler(String fileName) {
        this.fileName = fileName;
    }

    public abstract Map<K, V> readFromFile() throws IOException;

    public abstract void writeToFile(Map<K, V> map) throws IOException;

    public String getFileName() {
        return fileName;
    }
}
