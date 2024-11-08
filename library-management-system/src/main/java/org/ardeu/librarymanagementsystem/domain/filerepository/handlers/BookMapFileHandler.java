package org.ardeu.librarymanagementsystem.domain.filerepository.handlers;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.mappers.BookToTextMapper;
import org.ardeu.librarymanagementsystem.domain.entities.book.mappers.TextToBookMapper;
import org.ardeu.librarymanagementsystem.domain.filerepository.base.MapFileHandler;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A concrete implementation of {@link MapFileHandler} for handling book-related file operations.
 * This class provides functionality to read and write a map of book IDs to {@link Book} objects
 * from and to a text file. It uses mappers for converting between {@link Book} objects and their
 * text representations.
 */
public class BookMapFileHandler extends MapFileHandler<UUID, Book> {

    private final TextToBookMapper textToBookMapper;
    private final BookToTextMapper bookToTextMapper;

    /**
     * Constructs a new {@link BookMapFileHandler} with the specified file name.
     *
     * @param fileName the name of the file where book data will be read from or written to
     */
    public BookMapFileHandler(String fileName) {
        super(fileName);
        this.textToBookMapper = new TextToBookMapper();
        this.bookToTextMapper = new BookToTextMapper();
    }

    /**
     * Reads a map of book IDs to {@link Book} objects from the text file specified by {@code fileName}.
     * Each line of the file is mapped to a {@link Book} object.
     *
     * @return a map of book IDs to {@link Book} objects read from the file
     * @throws IOException if an I/O error occurs while reading the file
     */
    @Override
    public Map<UUID, Book> readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getFileName())))) {
            return reader
                    .lines()
                    .map(this.textToBookMapper)
                    .collect(Collectors.toMap(Book::getId, Function.identity()));
        }
        catch (EOFException e) {
            return Collections.emptyMap();
        }
    }

    /**
     * Writes a map of book IDs to {@link Book} objects to the text file specified by {@code fileName}.
     * Each book in the map is converted to its string representation using the {@link BookToTextMapper}.
     *
     * @param map the map of book IDs to {@link Book} objects to write to the file
     * @throws IOException if an I/O error occurs while writing the file
     */
    @Override
    public void writeToFile(Map<UUID, Book> map) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFileName())))) {
            for (String line : map.values().stream().map(this.bookToTextMapper).toList()) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
