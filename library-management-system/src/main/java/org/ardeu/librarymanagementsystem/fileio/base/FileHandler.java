package org.ardeu.librarymanagementsystem.fileio.base;

import java.io.IOException;
import java.util.List;

public abstract class FileHandler<T> {
    private final String fileName;
    public abstract List<T> readFromFile() throws IOException;
    public abstract void writeToFile(List<T> list) throws IOException;

    public FileHandler(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
