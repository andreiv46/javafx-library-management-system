package org.ardeu.librarymanagementsystem.fileio.handlers;

import org.ardeu.librarymanagementsystem.fileio.baseio.FileHandler;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class BinaryFileHandler<T extends Serializable> extends FileHandler<T> {
    public BinaryFileHandler(String fileName) {
        super(fileName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> readFromFile() throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.getFileName()))) {
            return (List<T>) in.readObject();
        } catch (EOFException | ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void writeToFile(List<T> list) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.getFileName()))) {
            out.writeObject(list);
        }
    }
}
