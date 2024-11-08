package org.ardeu.librarymanagementsystem.domain.entities.author;

/**
 * Enum representing the fields available for exporting author data to a CSV file.
 * Each field has a display name, used as the CSV header.
 */
public enum AuthorExportField {
    ID("ID"),
    NAME("Name"),
    BOOKS_COUNT("Books count"),
    TOTAL_LOANS("Total loans");

    /**
     * The display name of the export field.
     */
    private final String displayName;

    /**
     * Constructs an {@code AuthorExportField} with a specified display name.
     *
     * @param value the display name for the export field
     */
    AuthorExportField(String value) {
        this.displayName = value;
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
