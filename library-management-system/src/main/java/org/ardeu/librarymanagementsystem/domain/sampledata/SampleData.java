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
            Inventory inventory1 = new Inventory(UUID.randomUUID(), book1.getId(), 8, 10, 29.99);
            Inventory inventory2 = new Inventory(UUID.randomUUID(), book2.getId(), 3, 5, 34.99);
            Inventory inventory3 = new Inventory(UUID.randomUUID(), book3.getId(), 3, 5, 34.99);
            Inventory inventory4 = new Inventory(UUID.randomUUID(), book4.getId(), 6, 7, 24.99);
            Inventory inventory5 = new Inventory(UUID.randomUUID(), book5.getId(), 7, 8, 19.99);
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
            Loan[] loans = new Loan[]{
                    // Active loans
                    new Loan(UUID.randomUUID(), member1.getId(), book1.getId(), inventory1.getPrice(), LocalDate.of(2023, 12, 15), LocalDate.of(2023, 12, 15).plusDays(14), null, LoanStatus.ACTIVE),
                    new Loan(UUID.randomUUID(), member2.getId(), book2.getId(), inventory2.getPrice(), LocalDate.of(2024, 2, 1), LocalDate.of(2024, 2, 1).plusDays(15), null, LoanStatus.ACTIVE),
                    new Loan(UUID.randomUUID(), member3.getId(), book3.getId(), inventory3.getPrice(), LocalDate.of(2024, 6, 10), LocalDate.of(2024, 6, 10).plusDays(14), null, LoanStatus.ACTIVE),
                    new Loan(UUID.randomUUID(), member1.getId(), book1.getId(), inventory1.getPrice(), LocalDate.of(2024, 11, 6), LocalDate.of(2024, 11, 6).plusDays(10), null, LoanStatus.ACTIVE),
                    new Loan(UUID.randomUUID(), member2.getId(), book2.getId(), inventory2.getPrice(), LocalDate.of(2024, 11, 7), LocalDate.of(2024, 11, 7).plusDays(12), null, LoanStatus.ACTIVE),
                    new Loan(UUID.randomUUID(), member3.getId(), book3.getId(), inventory3.getPrice(), LocalDate.of(2024, 11, 8), LocalDate.of(2024, 11, 8).plusDays(14), null, LoanStatus.ACTIVE),
                    new Loan(UUID.randomUUID(), member1.getId(), book4.getId(), inventory4.getPrice(), LocalDate.of(2024, 11, 9), LocalDate.of(2024, 11, 9).plusDays(10), null, LoanStatus.ACTIVE),
                    new Loan(UUID.randomUUID(), member2.getId(), book5.getId(), inventory5.getPrice(), LocalDate.of(2024, 11, 10), LocalDate.of(2024, 11, 10).plusDays(13), null, LoanStatus.ACTIVE),

                    // returned books
                    new Loan(UUID.randomUUID(), member1.getId(), book4.getId(), inventory4.getPrice(), LocalDate.of(2023, 7, 1), LocalDate.of(2023, 7, 1).plusDays(12), LocalDate.of(2023, 7, 10), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book5.getId(), inventory5.getPrice(), LocalDate.of(2023, 8, 5), LocalDate.of(2023, 8, 5).plusDays(10), LocalDate.of(2023, 8, 14), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book6.getId(), inventory6.getPrice(), LocalDate.of(2023, 9, 12), LocalDate.of(2023, 9, 12).plusDays(8), LocalDate.of(2023, 9, 19), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book7.getId(), inventory7.getPrice(), LocalDate.of(2023, 10, 2), LocalDate.of(2023, 10, 2).plusDays(14), LocalDate.of(2023, 10, 16), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book8.getId(), inventory8.getPrice(), LocalDate.of(2023, 11, 10), LocalDate.of(2023, 11, 10).plusDays(15), LocalDate.of(2023, 11, 20), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book9.getId(), inventory9.getPrice(), LocalDate.of(2023, 12, 3), LocalDate.of(2023, 12, 3).plusDays(12), LocalDate.of(2023, 12, 15), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book10.getId(), inventory10.getPrice(), LocalDate.of(2024, 1, 15), LocalDate.of(2024, 1, 15).plusDays(10), LocalDate.of(2024, 1, 25), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book11.getId(), inventory11.getPrice(), LocalDate.of(2024, 2, 20), LocalDate.of(2024, 2, 20).plusDays(14), LocalDate.of(2024, 3, 5), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book12.getId(), inventory12.getPrice(), LocalDate.of(2024, 3, 5), LocalDate.of(2024, 3, 5).plusDays(10), LocalDate.of(2024, 3, 15), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book13.getId(), inventory13.getPrice(), LocalDate.of(2024, 4, 10), LocalDate.of(2024, 4, 10).plusDays(12), LocalDate.of(2024, 4, 22), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book14.getId(), inventory14.getPrice(), LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 1).plusDays(10), LocalDate.of(2024, 5, 10), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book1.getId(), inventory1.getPrice(), LocalDate.of(2024, 6, 3), LocalDate.of(2024, 6, 3).plusDays(14), LocalDate.of(2024, 6, 17), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book2.getId(), inventory2.getPrice(), LocalDate.of(2024, 7, 12), LocalDate.of(2024, 7, 12).plusDays(8), LocalDate.of(2024, 7, 20), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book3.getId(), inventory3.getPrice(), LocalDate.of(2024, 8, 25), LocalDate.of(2024, 8, 25).plusDays(12), LocalDate.of(2024, 9, 6), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book4.getId(), inventory4.getPrice(), LocalDate.of(2024, 9, 10), LocalDate.of(2024, 9, 10).plusDays(10), LocalDate.of(2024, 9, 20), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book5.getId(), inventory5.getPrice(), LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 1).plusDays(14), LocalDate.of(2024, 10, 15), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book6.getId(), inventory6.getPrice(), LocalDate.of(2024, 11, 5), LocalDate.of(2024, 11, 5).plusDays(8), LocalDate.of(2024, 11, 13), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book6.getId(), inventory6.getPrice(), LocalDate.of(2023, 12, 5), LocalDate.of(2023, 12, 5).plusDays(9), LocalDate.of(2023, 12, 14), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book7.getId(), inventory7.getPrice(), LocalDate.of(2024, 1, 12), LocalDate.of(2024, 1, 12).plusDays(12), LocalDate.of(2024, 1, 24), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book8.getId(), inventory8.getPrice(), LocalDate.of(2024, 2, 18), LocalDate.of(2024, 2, 18).plusDays(8), LocalDate.of(2024, 2, 26), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book9.getId(), inventory9.getPrice(), LocalDate.of(2024, 3, 15), LocalDate.of(2024, 3, 15).plusDays(10), LocalDate.of(2024, 3, 25), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book10.getId(), inventory10.getPrice(), LocalDate.of(2024, 4, 10), LocalDate.of(2024, 4, 10).plusDays(14), LocalDate.of(2024, 4, 24), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book11.getId(), inventory11.getPrice(), LocalDate.of(2024, 5, 5), LocalDate.of(2024, 5, 5).plusDays(9), LocalDate.of(2024, 5, 14), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book12.getId(), inventory12.getPrice(), LocalDate.of(2024, 6, 7), LocalDate.of(2024, 6, 7).plusDays(12), LocalDate.of(2024, 6, 19), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book13.getId(), inventory13.getPrice(), LocalDate.of(2024, 7, 15), LocalDate.of(2024, 7, 15).plusDays(14), LocalDate.of(2024, 7, 29), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book14.getId(), inventory14.getPrice(), LocalDate.of(2024, 8, 1), LocalDate.of(2024, 8, 1).plusDays(10), LocalDate.of(2024, 8, 11), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book1.getId(), inventory1.getPrice(), LocalDate.of(2024, 9, 7), LocalDate.of(2024, 9, 7).plusDays(13), LocalDate.of(2024, 9, 20), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book2.getId(), inventory2.getPrice(), LocalDate.of(2024, 10, 3), LocalDate.of(2024, 10, 3).plusDays(14), LocalDate.of(2024, 10, 17), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book3.getId(), inventory3.getPrice(), LocalDate.of(2024, 10, 10), LocalDate.of(2024, 10, 10).plusDays(8), LocalDate.of(2024, 10, 18), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member1.getId(), book4.getId(), inventory4.getPrice(), LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 1).plusDays(12), LocalDate.of(2024, 11, 13), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member2.getId(), book5.getId(), inventory5.getPrice(), LocalDate.of(2024, 11, 3), LocalDate.of(2024, 11, 3).plusDays(15), LocalDate.of(2024, 11, 18), LoanStatus.RETURNED),
                    new Loan(UUID.randomUUID(), member3.getId(), book6.getId(), inventory6.getPrice(), LocalDate.of(2024, 11, 4), LocalDate.of(2024, 11, 4).plusDays(12), LocalDate.of(2024, 11, 16), LoanStatus.RETURNED)
            };

            for (Loan loan : loans) {
                loanService.add(loan);
                if (loan.getMemberId().equals(member1.getId())) {
                    member1.getLoans().add(loan.getId());
                } else if (loan.getMemberId().equals(member2.getId())) {
                    member2.getLoans().add(loan.getId());
                } else {
                    member3.getLoans().add(loan.getId());
                }
            }

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