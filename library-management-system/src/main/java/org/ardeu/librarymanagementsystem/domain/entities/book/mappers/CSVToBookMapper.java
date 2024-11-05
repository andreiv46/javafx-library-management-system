package org.ardeu.librarymanagementsystem.domain.entities.book.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;

import java.time.LocalDate;
import java.util.UUID;
import java.util.function.Function;

public class CSVToBookMapper implements Function<String, Book> {
    @Override
    public Book apply(String s) {
        String[] values = s.split(",");
        return Book.builder()
                .setId(UUID.fromString(values[0]))
                .setTitle(values[1])
                .setDescription(values[2])
                .setPublishDate(LocalDate.parse(values[3]))
                .setAuthorId(UUID.fromString(values[4]))
                .setGenreId(UUID.fromString(values[5]))
                .build();
    }
}
