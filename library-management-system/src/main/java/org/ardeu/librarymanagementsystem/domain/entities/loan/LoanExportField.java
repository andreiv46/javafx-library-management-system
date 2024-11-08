package org.ardeu.librarymanagementsystem.domain.entities.loan;

/**
 * Enum representing the fields available for exporting loan information to a CSV file.
 * Each field corresponds to an attribute of a loan or related entities that can be included in the export.
 */
public enum LoanExportField {

    /**
     * The unique identifier of the loan.
     */
    ID("ID"),

    /**
     * The unique identifier of the book in the loan.
     */
    BOOK_ID("Book ID"),

    /**
     * The title of the book in the loan.
     */
    BOOK_TITLE("Book Title"),

    /**
     * The unique identifier of the member who borrowed the book.
     */
    MEMBER_ID("Member ID"),

    /**
     * The name of the member who borrowed the book.
     */
    MEMBER_NAME("Member Name"),

    /**
     * The email address of the member who borrowed the book.
     */
    MEMBER_EMAIL("Member Email"),

    /**
     * The date when the loan was made.
     */
    LOAN_DATE("Loan Date"),

    /**
     * The due date for returning the book.
     */
    DUE_DATE("Due Date"),

    /**
     * The date the book was returned, or null if not yet returned.
     */
    RETURN_DATE("Return Date"),

    /**
     * The status of the loan (e.g., borrowed, returned, overdue).
     */
    STATUS("Status"),

    /**
     * The price of the loan.
     */
    PRICE("Price"),

    /**
     * The unique identifier of the genre of the book.
     */
    GENRE_ID("Genre ID"),

    /**
     * The name of the genre of the book.
     */
    GENRE_NAME("Genre Name"),

    /**
     * The unique identifier of the author of the book.
     */
    AUTHOR_ID("Author ID"),

    /**
     * The name of the author of the book.
     */
    AUTHOR_NAME("Author Name");

    private final String displayName;

    /**
     * Constructs a LoanExportField with the specified display name.
     *
     * @param value the display name of the field
     */
    LoanExportField(String value) {
        this.displayName = value;
    }

    /**
     * Returns the display name of the field.
     *
     * @return the display name of the field
     */
    public String getDisplayName() {
        return displayName;
    }
}
