package org.ardeu.librarymanagementsystem.domain.entities.genre;

/**
 * Enum representing the fields that can be exported for a genre in the library management system.
 * Each field corresponds to a property of the genre, which can be selected for export in a CSV format.
 */
public enum GenreExportField {

    /**
     * Represents the unique identifier of the genre.
     */
    ID("ID"),

    /**
     * Represents the name of the genre.
     */
    NAME("Name"),

    /**
     * Represents the count of books associated with the genre.
     */
    BOOKS_COUNT("Books count"),

    /**
     * Represents the total number of loans for books within the genre.
     */
    TOTAL_LOANS("Total loans");

    private final String displayName;

    /**
     * Constructs a GenreExportField with the specified display name.
     *
     * @param name the display name for the field
     */
    GenreExportField(String name) {
        this.displayName = name;
    }

    /**
     * Gets the display name for the field, which will be shown in the exported CSV header.
     *
     * @return the display name for the field
     */
    public String getDisplayName() {
        return displayName;
    }
}
