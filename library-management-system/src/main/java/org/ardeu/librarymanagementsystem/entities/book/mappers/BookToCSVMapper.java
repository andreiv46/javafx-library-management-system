package org.ardeu.librarymanagementsystem.entities.book.mappers;

import org.ardeu.librarymanagementsystem.entities.book.Book;

import java.util.function.Function;

public class BookToCSVMapper implements Function<Book, String> {
    @Override
    public String apply(Book book) {
        return String.join(",",
                book.getId().toString(),
                book.getTitle(),
                book.getDescription(),
                book.getPublishDate().toString(),
                book.getAuthorId().toString(),
                book.getGenreId().toString()
        );
    }
}
