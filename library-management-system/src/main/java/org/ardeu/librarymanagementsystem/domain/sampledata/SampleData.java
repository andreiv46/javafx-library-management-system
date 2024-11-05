package org.ardeu.librarymanagementsystem.domain.sampledata;

import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanStatus;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.exceptions.entity.DuplicateItemException;
import org.ardeu.librarymanagementsystem.domain.filerepository.config.FilePathConfig;
import org.ardeu.librarymanagementsystem.domain.services.*;
import org.ardeu.librarymanagementsystem.domain.services.registry.ServiceRegistry;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

public class SampleData {

    private final BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private final GenreService genreService = ServiceRegistry.getInstance().getService(GenreService.class);
    private final InventoryService inventoryService = ServiceRegistry.getInstance().getService(InventoryService.class);
    private final MemberService memberService = ServiceRegistry.getInstance().getService(MemberService.class);
    private final LoanService loanService = ServiceRegistry.getInstance().getService(LoanService.class);


    public void populateData() {
        try {
            // Sample Authors
            Author author1 = new Author(UUID.randomUUID(), "J.K. Rowling", new HashSet<>());
            Author author2 = new Author(UUID.randomUUID(), "George R.R. Martin", new HashSet<>());
            Author author3 = new Author(UUID.randomUUID(), "J.R.R. Tolkien", new HashSet<>());
            Author author4 = new Author(UUID.randomUUID(), "Isaac Asimov", new HashSet<>());
            Author author5 = new Author(UUID.randomUUID(), "Arthur C. Clarke", new HashSet<>());

            authorService.add(author1);
            authorService.add(author2);
            authorService.add(author3);
            authorService.add(author4);
            authorService.add(author5);

            // Sample Genres
            Genre genre1 = new Genre(UUID.randomUUID(), "Fantasy", new HashSet<>());
            Genre genre2 = new Genre(UUID.randomUUID(), "Adventure", new HashSet<>());
            Genre genre3 = new Genre(UUID.randomUUID(), "Mystery", new HashSet<>());
            Genre genre4 = new Genre(UUID.randomUUID(), "Science Fiction", new HashSet<>());

            genreService.add(genre1);
            genreService.add(genre2);
            genreService.add(genre3);
            genreService.add(genre4);

            // Sample Books
            Book book1 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Harry Potter and the Sorcerer's Stone")
                    .setDescription("A young wizard's journey begins")
                    .setPublishDate(LocalDate.of(1997, 6, 26))
                    .setAuthorId(author1.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book2 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("A Game of Thrones")
                    .setDescription("A tale of power family and intrigue")
                    .setPublishDate(LocalDate.of(1996, 8, 6))
                    .setAuthorId(author2.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book3 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("A Clash of Kings")
                    .setDescription("The second book in the A Song of Ice and Fire series")
                    .setPublishDate(LocalDate.of(1998, 11, 16))
                    .setAuthorId(author2.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book4 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("The Hobbit")
                    .setDescription("A hobbit's adventure")
                    .setPublishDate(LocalDate.of(1937, 9, 21))
                    .setAuthorId(author3.getId())
                    .setGenreId(genre2.getId())
                    .build();

            Book book5 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("The Fellowship of the Ring")
                    .setDescription("The first part of the Lord of the Rings")
                    .setPublishDate(LocalDate.of(1954, 7, 29))
                    .setAuthorId(author3.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book6 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Foundation")
                    .setDescription("A science fiction classic")
                    .setPublishDate(LocalDate.of(1951, 5, 1))
                    .setAuthorId(author4.getId())
                    .setGenreId(genre4.getId())
                    .build();

            Book book7 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("I Robot")
                    .setDescription("A collection of robot stories")
                    .setPublishDate(LocalDate.of(1950, 12, 2))
                    .setAuthorId(author4.getId())
                    .setGenreId(genre4.getId())
                    .build();

            Book book8 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("2001: A Space Odyssey")
                    .setDescription("A journey through space")
                    .setPublishDate(LocalDate.of(1968, 7, 1))
                    .setAuthorId(author5.getId())
                    .setGenreId(genre4.getId())
                    .build();

            Book book9 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Rendezvous with Rama")
                    .setDescription("A mysterious alien spacecraft")
                    .setPublishDate(LocalDate.of(1973, 6, 1))
                    .setAuthorId(author5.getId())
                    .setGenreId(genre4.getId())
                    .build();

            Book book10 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Harry Potter and the Chamber of Secrets")
                    .setDescription("The second book in the Harry Potter series")
                    .setPublishDate(LocalDate.of(1998, 7, 2))
                    .setAuthorId(author1.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book11 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Harry Potter and the Prisoner of Azkaban")
                    .setDescription("The third book in the Harry Potter series")
                    .setPublishDate(LocalDate.of(1999, 7, 8))
                    .setAuthorId(author1.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book12 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Harry Potter and the Goblet of Fire")
                    .setDescription("The fourth book in the Harry Potter series")
                    .setPublishDate(LocalDate.of(2000, 7, 8))
                    .setAuthorId(author1.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book13 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Harry Potter and the Order of the Phoenix")
                    .setDescription("The fifth book in the Harry Potter series")
                    .setPublishDate(LocalDate.of(2003, 6, 21))
                    .setAuthorId(author1.getId())
                    .setGenreId(genre1.getId())
                    .build();

            Book book14 = Book.builder()
                    .setId(UUID.randomUUID())
                    .setTitle("Harry Potter and the Half-Blood Prince")
                    .setDescription("The sixth book in the Harry Potter series")
                    .setPublishDate(LocalDate.of(2005, 7, 16))
                    .setAuthorId(author1.getId())
                    .setGenreId(genre1.getId())
                    .build();

            bookService.add(book1);
            bookService.add(book2);
            bookService.add(book3);
            bookService.add(book4);
            bookService.add(book5);
            bookService.add(book6);
            bookService.add(book7);
            bookService.add(book8);
            bookService.add(book9);
            bookService.add(book10);
            bookService.add(book11);
            bookService.add(book12);
            bookService.add(book13);
            bookService.add(book14);

            // Link books to authors and genres
            author1.getBooks().add(book1.getId());
            author1.getBooks().add(book10.getId());
            author1.getBooks().add(book11.getId());
            author1.getBooks().add(book13.getId());
            author1.getBooks().add(book14.getId());
            author1.getBooks().add(book12.getId());
            author2.getBooks().add(book2.getId());
            author2.getBooks().add(book3.getId());
            author3.getBooks().add(book4.getId());
            author3.getBooks().add(book5.getId());
            author4.getBooks().add(book6.getId());
            author4.getBooks().add(book7.getId());
            author5.getBooks().add(book8.getId());
            author5.getBooks().add(book9.getId());

            genre1.getBooks().add(book1.getId());
            genre1.getBooks().add(book2.getId());
            genre1.getBooks().add(book3.getId());
            genre1.getBooks().add(book5.getId());
            genre1.getBooks().add(book10.getId());
            genre1.getBooks().add(book11.getId());
            genre1.getBooks().add(book12.getId());
            genre1.getBooks().add(book13.getId());
            genre1.getBooks().add(book14.getId());
            genre2.getBooks().add(book4.getId());
            genre4.getBooks().add(book6.getId());
            genre4.getBooks().add(book7.getId());
            genre4.getBooks().add(book8.getId());
            genre4.getBooks().add(book9.getId());

            // Sample Members
            Member member1 = new Member(UUID.randomUUID(), "Alice Smith", "alice@example.com", new HashSet<>());
            Member member2 = new Member(UUID.randomUUID(), "Bob Johnson", "bob@example.com", new HashSet<>());
            Member member3 = new Member(UUID.randomUUID(), "Charlie Brown", "charlie@example.com", new HashSet<>());

            memberService.add(member1);
            memberService.add(member2);
            memberService.add(member3);

            // Sample Inventory
            Inventory inventory1 = new Inventory(UUID.randomUUID(), book1.getId(), 10, 10, 29.99);
            Inventory inventory2 = new Inventory(UUID.randomUUID(), book2.getId(), 5, 5, 34.99);
            Inventory inventory3 = new Inventory(UUID.randomUUID(), book3.getId(), 4, 5, 34.99);
            Inventory inventory4 = new Inventory(UUID.randomUUID(), book4.getId(), 7, 7, 24.99);
            Inventory inventory5 = new Inventory(UUID.randomUUID(), book5.getId(), 8, 8, 19.99);
            Inventory inventory6 = new Inventory(UUID.randomUUID(), book6.getId(), 12, 12, 39.99);
            Inventory inventory7 = new Inventory(UUID.randomUUID(), book7.getId(), 10, 10, 29.99);
            Inventory inventory8 = new Inventory(UUID.randomUUID(), book8.getId(), 6, 6, 34.99);
            Inventory inventory9 = new Inventory(UUID.randomUUID(), book9.getId(), 8, 8, 24.99);
            Inventory inventory10 = new Inventory(UUID.randomUUID(), book10.getId(), 10, 10, 29.99);
            Inventory inventory11 = new Inventory(UUID.randomUUID(), book11.getId(), 10, 10, 29.99);
            Inventory inventory12 = new Inventory(UUID.randomUUID(), book12.getId(), 10, 10, 29.99);
            Inventory inventory13 = new Inventory(UUID.randomUUID(), book13.getId(), 10, 10, 29.99);
            Inventory inventory14 = new Inventory(UUID.randomUUID(), book14.getId(), 0, 0, 29.99);

            inventoryService.add(inventory1);
            inventoryService.add(inventory2);
            inventoryService.add(inventory3);
            inventoryService.add(inventory4);
            inventoryService.add(inventory5);
            inventoryService.add(inventory6);
            inventoryService.add(inventory7);
            inventoryService.add(inventory8);
            inventoryService.add(inventory9);
            inventoryService.add(inventory10);
            inventoryService.add(inventory11);
            inventoryService.add(inventory12);
            inventoryService.add(inventory13);
            inventoryService.add(inventory14);

            // Sample Loans
            Loan loan1 = new Loan(UUID.randomUUID(), member1.getId(), book1.getId(), inventory1.getPrice(),
                    LocalDate.of(2024, 10, 3),
                    LocalDate.of(2024, 10, 3).plusDays(14),
                    LocalDate.of(2024, 10, 5),
                    LoanStatus.RETURNED
            );
            Loan loan2 = new Loan(UUID.randomUUID(), member1.getId(), book2.getId(), inventory2.getPrice(),
                    LocalDate.of(2024, 10, 5),
                    LocalDate.of(2024, 10, 5).plusDays(10),
                    LocalDate.of(2024, 10, 15),
                    LoanStatus.RETURNED
            );
            Loan loan3 = new Loan(UUID.randomUUID(), member2.getId(), book3.getId(), inventory3.getPrice(),
                    LocalDate.of(2024, 10, 2),
                    LocalDate.of(2024, 10, 2).plusDays(12),
                    LocalDate.of(2024, 10, 2).plusDays(12),
                    LoanStatus.RETURNED);

            Loan loan4 = new Loan(UUID.randomUUID(), member2.getId(), book3.getId(), inventory3.getPrice(),
                    LocalDate.now(),
                    LocalDate.now().plusDays(14),
                    null,
                    LoanStatus.ACTIVE);

            loanService.add(loan1);
            loanService.add(loan2);
            loanService.add(loan3);
            loanService.add(loan4);

            // Link loans to members
            member1.getLoans().add(loan1.getId());
            member1.getLoans().add(loan2.getId());
            member2.getLoans().add(loan3.getId());
        } catch (DuplicateItemException e) {
            System.err.println("Error adding sample data: " + e.getMessage());
        }
        System.out.println("Sample data generated successfully!");
    }

    public void resetFiles(){
        String[] filePaths = {
                FilePathConfig.AUTHORS_PATH,
                FilePathConfig.BOOKS_PATH,
                FilePathConfig.GENRES_PATH,
                FilePathConfig.INVENTORIES_PATH,
                FilePathConfig.LOANS_PATH,
                FilePathConfig.MEMBERS_PATH
        };

        for (String path : filePaths) {
            File file = new File(path);

            try {
                if (file.exists()) {
                    Files.delete(file.toPath());
                    System.out.println("Deleted file: " + path);
                    Files.createFile(Paths.get(path));
                    System.out.println("Created new file: " + path);
                }
            } catch (IOException e) {
                System.err.println("Error handling file: " + path);
                e.printStackTrace();
            }
        }
    }
}

