package org.ardeu.librarymanagementsystem.domain.filerepository.handlers;

import org.ardeu.librarymanagementsystem.domain.exceptions.file.DataFormatException;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A concrete implementation of {@link MapFileHandler} for handling binary file operations.
 * This class provides functionality to read and write a map of key-value pairs to and from a binary file.
 * Both the keys and values must be {@link Serializable}.
 *
 * @param <K> the type of keys maintained by the map (must be {@link Serializable})
 * @param <V> the type of values maintained by the map (must be {@link Serializable})
 */
public class BinaryMapFileHandler<K extends Serializable, V extends Serializable> extends MapFileHandler<K, V> {

    /**
     * Constructs a new {@link BinaryMapFileHandler} with the specified file name.
     *
     * @param fileName the name of the file where data will be read from or written to
     */
    public BinaryMapFileHandler(String fileName) {
        super(fileName);
    }

    /**
     * Reads a map of key-value pairs from the binary file specified by {@code fileName}.
     *
     * @return a map of key-value pairs read from the file
     * @throws IOException if an I/O error occurs while reading the file
     * @throws DataFormatException if the data format is incorrect or a class is not found
     */
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

    /**
     * Writes the specified map of key-value pairs to the binary file specified by {@code fileName}.
     *
     * @param map the map of key-value pairs to write to the file
     * @throws IOException if an I/O error occurs while writing the file
     */
    @Override
    public void writeToFile(Map<K, V> map) throws IOException {
        Map<K, V> mapCopy = new HashMap<>(map);
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(this.getFileName())))) {
            out.writeObject(mapCopy);
        }
    }
}
