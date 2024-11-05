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

public class InventoryMapFileHandler extends MapFileHandler<UUID, Inventory> {

    private final InventoryToCSVMapper inventoryToCSVMapper;
    private final CSVToInventoryMapper csvToInventoryMapper;

    public InventoryMapFileHandler(String fileName) {
        super(fileName);
        this.inventoryToCSVMapper = new InventoryToCSVMapper();
        this.csvToInventoryMapper = new CSVToInventoryMapper();
    }

    @Override
    public Map<UUID, Inventory> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToInventoryMapper)
                    .collect(Collectors.toMap(Inventory::getId, Function.identity()));
        }
        catch (EOFException e){
            return Collections.emptyMap();
        }
    }

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
