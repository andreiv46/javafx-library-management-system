package org.ardeu.librarymanagementsystem.domain.entities.book;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a book in the library management system.
 * <p>
 * This class uses the builder pattern for flexible object construction.
 * </p>
 *
 * @see BaseEntity
 */
public class Book extends BaseEntity {
    /**
     * The title of the book.
     */
    private final String title;

    /**
     * A description of the book's content or theme.
     */
    private final String description;

    /**
     * The date the book was published.
     */
    private final LocalDate publishDate;

    /**
     * The id of the author who wrote the book.
     */
    private final UUID authorId;

    /**
     * The id of the genre associated with the book.
     */
    private final UUID genreId;

    private Book(BookBuilder builder) {
        super(builder.id);
        this.title = builder.title;
        this.description = builder.description;
        this.publishDate = builder.publishDate;
        this.authorId = builder.authorId;
        this.genreId = builder.genreId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getGenreId() {
        return genreId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", publishDate=" + publishDate +
                ", authorId=" + authorId +
                ", genreId=" + genreId +
                '}';
    }

    /**
     * Indicates whether some other object is equal to this one.
     * <p>
     * This implementation compares {@code Book} objects based on the title,
     * publish date, and author ID fields only, as they are considered sufficient
     * for identifying a unique book.
     * </p>
     *
     * @param o the object to compare this {@code Book} with for equality
     * @return {@code true} if this object is the same as the {@code o} argument;
     * {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(publishDate, book.publishDate) && Objects.equals(authorId, book.authorId);
    }

    /**
     * Returns a hash code value for this book.
     * <p>
     * This implementation generates the hash code based on the title, publish date,
     * and author ID fields, as they are used in the {@code equals} method to establish
     * equality.
     * </p>
     *
     * @return a hash code value for this book
     */
    @Override
    public int hashCode() {
        return Objects.hash(title, publishDate, authorId);
    }

    /**
     * Returns a new instance of the {@code BookBuilder} for constructing {@code Book} objects.
     *
     * @return a new {@code BookBuilder}
     */
    public static BookBuilder builder(){
        return new BookBuilder();
    }

    /**
     * Builder class for {@link Book}. Allows flexible and readable construction of {@code Book} instances.
     */
    public static class BookBuilder{
        private UUID id;
        private String title;
        private String description;
        private LocalDate publishDate;
        private UUID authorId;
        private UUID genreId;

        public BookBuilder setId(UUID id) {
            this.id = id;
            return this;
        }

        public BookBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public BookBuilder setPublishDate(LocalDate publishDate) {
            this.publishDate = publishDate;
            return this;
        }

        public BookBuilder setAuthorId(UUID authorId) {
            this.authorId = authorId;
            return this;
        }

        public BookBuilder setGenreId(UUID genreId) {
            this.genreId = genreId;
            return this;
        }

        public BookBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         * Builds a {@link Book} instance with the specified properties.
         *
         * @return a new {@code Book} instance
         */
        public Book build(){
            return new Book(this);
        }
    }
}
