package org.ardeu.librarymanagementsystem.domain.entities.book.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

/**
 * A mapper that converts a formatted text string into a Book object.
 */
public class TextToBookMapper implements Function<String, Book> {

    /**
     * Converts a formatted string into a Book object.
     *
     * @param s the formatted string containing book details
     * @return a Book object with the details parsed from the input string
     * @throws IllegalArgumentException if the input string does not match the expected format
     */
    @Override
    public Book apply(String s) {
        String[] values = s.split("\\|\\|");
        if(values.length != 6) {
            throw new IllegalArgumentException("Invalid book text format");
        }
        return Book.builder()
                .setId(UUID.fromString(values[0].split(": ", 2)[1]))
                .setTitle(values[1].split(": ", 2)[1])
                .setDescription(values[2].split(": ", 2)[1])
                .setPublishDate(LocalDate.parse(values[3].split(": ", 2)[1]))
                .setAuthorId(UUID.fromString(values[4].split(": ", 2)[1]))
                .setGenreId(UUID.fromString(values[5].split(": ", 2)[1]))
                .build();
    }
}
