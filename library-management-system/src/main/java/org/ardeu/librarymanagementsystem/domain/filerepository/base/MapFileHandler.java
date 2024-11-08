package org.ardeu.librarymanagementsystem.domain.filerepository.base;

import java.io.IOException;
import java.util.Map;

/**
 * Abstract class for handling file operations related to a map (key-value pairs).
 * Provides methods for reading from and writing to a file that stores the map data.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public abstract class MapFileHandler<K, V> {

    private final String fileName;

    /**
     * Constructs a MapFileHandler with the given file name.
     *
     * @param fileName the name of the file to read from or write to
     */
    public MapFileHandler(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Reads the map data from the file and returns it as a map.
     *
     * @return a map containing the data read from the file
     * @throws IOException if an I/O error occurs while reading from the file
     */
    public abstract Map<K, V> readFromFile() throws IOException;

    /**
     * Writes the given map data to the file.
     *
     * @param map the map to be written to the file
     * @throws IOException if an I/O error occurs while writing to the file
     */
    public abstract void writeToFile(Map<K, V> map) throws IOException;

    /**
     * Retrieves the file name associated with this MapFileHandler.
     *
     * @return the name of the file
     */
    public String getFileName() {
        return fileName;
    }
}

