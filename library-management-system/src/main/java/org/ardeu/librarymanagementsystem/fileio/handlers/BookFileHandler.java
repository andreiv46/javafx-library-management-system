package org.ardeu.librarymanagementsystem.fileio.handlers;

import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.entities.book.mappers.BookToCSVMapper;
import org.ardeu.librarymanagementsystem.entities.book.mappers.CSVToBookMapper;
import org.ardeu.librarymanagementsystem.fileio.baseio.FileHandler;

import java.io.*;
import java.util.Collections;
import java.util.List;

public class BookFileHandler extends FileHandler<Book> {

    private final CSVToBookMapper csvToBookMapper;
    private final BookToCSVMapper bookToCSVMapper;

    public BookFileHandler(String fileName) {
        super(fileName);
        this.csvToBookMapper = new CSVToBookMapper();
        this.bookToCSVMapper = new BookToCSVMapper();
    }

    @Override
    public List<Book> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.csvToBookMapper)
                    .toList();
        }
        catch (EOFException e){
            return Collections.emptyList();
        }
    }

    @Override
    public void writeToFile(List<Book> list) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFileName())))) {
            list.stream()
                    .map(this.bookToCSVMapper)
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
