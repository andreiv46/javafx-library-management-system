package org.ardeu.librarymanagementsystem.domain.entities.book.mappers;

import org.ardeu.librarymanagementsystem.domain.entities.book.Book;

import java.util.function.Function;

/**
 * A mapper that converts a Book object into a formatted text representation.
 * This is used for exporting or displaying book details as a string.
 * @see Book
 * @see Function
 */
public class BookToTextMapper implements Function<Book, String> {
    /**
     * Converts a Book object into a formatted string.
     * The resulting string contains the book's ID, title, description, publication date, author ID, and genre ID.
     *
     * @param book the Book object to be converted
     * @return a formatted string containing the details of the book
     */
    @Override
    public String apply(Book book) {
        return "ID: " + book.getId() + "|| " +
                "Title: " + book.getTitle() + "|| " +
                "Description: " + book.getDescription() + "|| " +
                "Publication Date: " + book.getPublishDate() + "|| " +
                "Author ID: " + book.getAuthorId() + "|| " +
                "Genre ID: " + book.getGenreId();
    }
}
