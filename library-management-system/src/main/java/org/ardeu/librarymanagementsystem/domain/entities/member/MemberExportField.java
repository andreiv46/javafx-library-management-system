package org.ardeu.librarymanagementsystem.domain.entities.member;

/**
 * Enum representing the fields available for exporting member data.
 * This enum is used to define the fields that can be included in a member export (e.g., CSV).
 */
public enum MemberExportField {

    /**
     * Represents the unique identifier of the member.
     */
    ID("ID"),

    /**
     * Represents the name of the member.
     */
    NAME("Name"),

    /**
     * Represents the email address of the member.
     */
    EMAIL("Email"),

    /**
     * Represents the total number of loans made by the member.
     */
    TOTAL_LOANS("Total loans");

    private final String displayName;

    /**
     * Constructs a MemberExportField with a display name.
     *
     * @param displayName The name to be displayed for this field in the export.
     */
    MemberExportField(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retrieves the display name of the field.
     *
     * @return The display name associated with this field.
     */
    public String getDisplayName() {
        return displayName;
    }
}