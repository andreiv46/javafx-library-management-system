package org.ardeu.librarymanagementsystem.domain.services.base;

import java.io.IOException;

/**
 * The {@code DataService} interface defines the contract for services that handle saving
 * and loading data. Implementing classes are expected to provide functionality to persist
 * and retrieve data to/from a persistent storage medium, such as files or databases.
 */
public interface DataService {

    /**
     * Saves data to the persistent storage. Implementing classes should define the exact
     * method for saving the data (e.g., to a file or database).
     *
     * @throws IOException if an I/O error occurs while saving data
     */
    void save() throws IOException;

    /**
     * Loads data from the persistent storage. Implementing classes should define the exact
     * method for loading the data (e.g., from a file or database).
     *
     * @throws IOException if an I/O error occurs while loading data
     */
    void load() throws IOException;
}
