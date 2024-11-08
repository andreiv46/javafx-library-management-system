package org.ardeu.librarymanagementsystem.domain.filerepository.handlers;

import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.mappers.CSVToInventoryMapper;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.mappers.InventoryToCSVMapper;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A concrete implementation of {@link MapFileHandler} for handling inventory-related file operations.
 * This class provides functionality to read and write a map of inventory IDs to {@link Inventory} objects
 * from and to a CSV file. It uses mappers for converting between {@link Inventory} objects and their
 * CSV representations.
 */
public class InventoryMapFileHandler extends MapFileHandler<UUID, Inventory> {

    private final InventoryToCSVMapper inventoryToCSVMapper;
    private final CSVToInventoryMapper csvToInventoryMapper;

    /**
     * Constructs a new {@link InventoryMapFileHandler} with the specified file name.
     *
     * @param fileName the name of the file where inventory data will be read from or written to
     */
    public InventoryMapFileHandler(String fileName) {
        super(fileName);
        this.inventoryToCSVMapper = new InventoryToCSVMapper();
        this.csvToInventoryMapper = new CSVToInventoryMapper();
    }

    /**
     * Reads a map of inventory IDs to {@link Inventory} objects from the CSV file specified by {@code fileName}.
     * Each line of the file is mapped to an {@link Inventory} object.
     *
     * @return a map of inventory IDs to {@link Inventory} objects read from the file
     * @throws IOException if an I/O error occurs while reading the file
     */
    @Override
    public Map<UUID, Inventory> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToInventoryMapper)
                    .collect(Collectors.toMap(Inventory::getId, Function.identity()));
        }
        catch (EOFException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * Writes a map of inventory IDs to {@link Inventory} objects to the CSV file specified by {@code fileName}.
     * Each inventory object in the map is converted to its string representation using the {@link InventoryToCSVMapper}.
     *
     * @param map the map of inventory IDs to {@link Inventory} objects to write to the file
     * @throws IOException if an I/O error occurs while writing the file
     */
    @Override
    public void writeToFile(Map<UUID, Inventory> map) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFileName())))) {
            for (String line : map.values().stream().map(this.inventoryToCSVMapper).toList()) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
