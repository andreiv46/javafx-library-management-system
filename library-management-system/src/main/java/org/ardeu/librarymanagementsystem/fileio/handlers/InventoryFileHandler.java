package org.ardeu.librarymanagementsystem.fileio.handlers;

import org.ardeu.librarymanagementsystem.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.entities.inventory.mappers.CSVToInventoryMapper;
import org.ardeu.librarymanagementsystem.entities.inventory.mappers.InventoryToCSVMapper;
import org.ardeu.librarymanagementsystem.fileio.base.FileHandler;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class InventoryFileHandler extends FileHandler<Inventory> {

    private final InventoryToCSVMapper inventoryToCSVMapper;
    private final CSVToInventoryMapper csvToInventoryMapper;

    public InventoryFileHandler(String fileName) {
        super(fileName);
        this.inventoryToCSVMapper = new InventoryToCSVMapper();
        this.csvToInventoryMapper = new CSVToInventoryMapper();
    }

    @Override
    public List<Inventory> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToInventoryMapper)
                    .toList();
        }
        catch (EOFException e){
            return Collections.emptyList();
        }
    }

    @Override
    public void writeToFile(List<Inventory> list) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFileName())))) {
            list.stream()
                    .map(this.inventoryToCSVMapper)
                    .forEach(line -> {
                        try {
                            writer.write(line);
                            writer.newLine();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
