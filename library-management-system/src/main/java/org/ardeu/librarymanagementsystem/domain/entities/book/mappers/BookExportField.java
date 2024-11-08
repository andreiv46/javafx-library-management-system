package org.ardeu.librarymanagementsystem.domain.entities.book.mappers;

/**
 * Enum representing the fields that can be included in a book export.
 * Each field corresponds to a specific piece of information about a book that can be displayed in an export (e.g., CSV).
 */
public enum BookExportField {
    /**
     * The unique identifier of the book.
     */
    ID("ID"),

    /**
     * The title of the book.
     */
    TITLE("Title"),

    /**
     * The unique identifier of the author of the book.
     */
    AUTHOR_ID("Author ID"),

    /**
     * The name of the author of the book.
     */
    AUTHOR_NAME("Author Name"),

    /**
     * The publication date of the book.
     */
    PUBLICATION_DATE("Publication Date"),

    /**
     * The unique identifier of the genre of the book.
     */
    GENRE_ID("Genre ID"),

    /**
     * The name of the genre of the book.
     */
    GENRE_NAME("Genre Name"),

    /**
     * The number of available copies of the book.
     */
    BOOKS_AVAILABLE("Books Available"),

    /**
     * The total number of books in stock (including borrowed and available copies).
     */
    BOOKS_STOCK("Books Stock"),

    /**
     * The price of the book.
     */
    BOOKS_PRICE("Books Price"),

    /**
     * The total number of loans associated with the book.
     */
    TOTAL_LOANS("Total Loans");

    private final String displayName;

    /**
     * Constructs a BookExportField with the specified display name.
     *
     * @param displayName the name to be displayed for this field in exports
     */
    BookExportField(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retrieves the display name associated with this field.
     *
     * @return the display name of the field
     */
    public String getDisplayName() {
        return displayName;
    }
}

