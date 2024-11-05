package org.ardeu.librarymanagementsystem.domain.entities.book;

import org.ardeu.librarymanagementsystem.domain.entities.base.BaseEntity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Book extends BaseEntity {
    private String title;
    private String description;
    private LocalDate publishDate;
    private UUID authorId;
    private UUID genreId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(publishDate, book.publishDate) && Objects.equals(authorId, book.authorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, publishDate, authorId);
    }

    public static BookBuilder builder(){
        return new BookBuilder();
    }

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

        public Book build(){
            validate();
            return new Book(this);
        }

        private void validate() {
            StringBuilder message = new StringBuilder();
            this.id = Objects.requireNonNullElse(this.id, UUID.randomUUID());

            if (Objects.isNull(this.title)) {
                message.append("Title cannot be null\n");
            } else if (this.title.length() < 2) {
                message.append("Title must have at least 2 characters\n");
            }

            if (Objects.isNull(this.description)) {
                message.append("Description cannot be null\n");
            } else if (this.description.length() < 2) {
                message.append("Description must have at least 2 characters\n");
            }

            if (Objects.isNull(this.authorId)) {
                message.append("Author Id cannot be null\n");
            }

            if (Objects.isNull(this.genreId)) {
                message.append("Genre Id cannot be null\n");
            }

            if(Objects.nonNull(this.publishDate) && this.publishDate.isAfter(LocalDate.now())){
                message.append("Publish date must be greater than current date\n");
            }

            if(!message.isEmpty()){
                throw new IllegalArgumentException(String.valueOf(message));
            }
        }
    }
}
