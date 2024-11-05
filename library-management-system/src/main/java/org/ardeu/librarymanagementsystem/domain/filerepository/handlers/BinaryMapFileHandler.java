package org.ardeu.librarymanagementsystem.domain.filerepository.handlers;

import org.ardeu.librarymanagementsystem.domain.exceptions.file.DataFormatException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.*;
import java.text.Format;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BinaryMapFileHandler<K extends Serializable, V extends Serializable> extends MapFileHandler<K, V> {
    public BinaryMapFileHandler(String fileName) {
        super(fileName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<K, V> readFromFile() throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(this.getFileName())))) {
            return (Map<K, V>) in.readObject();
        } catch (EOFException e) {
            return Collections.emptyMap();
        } catch (ClassNotFoundException e) {
            throw new DataFormatException("Data format issue: class not found.", e);
        }
    }

    @Override
    public void writeToFile(Map<K, V> map) throws IOException {
        Map<K, V> mapCopy = new HashMap<>(map);
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(this.getFileName())))) {
            out.writeObject(mapCopy);
        }
    }
}
