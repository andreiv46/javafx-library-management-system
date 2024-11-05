package org.ardeu.librarymanagementsystem.domain.filerepository.handlers;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.mappers.BookToCSVMapper;
import org.ardeu.librarymanagementsystem.domain.entities.book.mappers.CSVToBookMapper;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BookMapFileHandler extends MapFileHandler<UUID, Book> {

    private final CSVToBookMapper csvToBookMapper;
    private final BookToCSVMapper bookToCSVMapper;

    public BookMapFileHandler(String fileName) {
        super(fileName);
        this.csvToBookMapper = new CSVToBookMapper();
        this.bookToCSVMapper = new BookToCSVMapper();
    }

    @Override
    public Map<UUID, Book> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToBookMapper)
                    .collect(Collectors.toMap(Book::getId, Function.identity()));
        }
        catch (EOFException e){
            return Collections.emptyMap();
        }
    }

    @Override
    public void writeToFile(Map<UUID, Book> map) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFileName())))) {
            for(String line : map.values().stream().map(this.bookToCSVMapper).toList()){
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
