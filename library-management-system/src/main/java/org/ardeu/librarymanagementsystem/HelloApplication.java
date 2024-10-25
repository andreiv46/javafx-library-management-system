package org.ardeu.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ardeu.librarymanagementsystem.entities.author.Author;
import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.fileio.config.FilePathConfig;
import org.ardeu.librarymanagementsystem.fileio.handlers.BinaryFileHandler;
import org.ardeu.librarymanagementsystem.fileio.handlers.BookFileHandler;
import org.ardeu.librarymanagementsystem.fileio.handlers.InventoryFileHandler;
import org.ardeu.librarymanagementsystem.fileio.handlers.LoanFileHandler;
import org.ardeu.librarymanagementsystem.managers.BookManager;
import org.ardeu.librarymanagementsystem.services.*;
import org.ardeu.librarymanagementsystem.services.registry.ServiceRegistry;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.UUID;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        stage.setOnCloseRequest(_ -> {
            BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);
            AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
            try {
                bookService.save();
                authorService.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        stage.show();

    }

    public static void main(String[] args) throws Exception {

        configureServices();

        BookManager manager = new BookManager();
        UUID authorId = UUID.randomUUID();
        UUID genreId = UUID.randomUUID();

        Book.BookBuilder builder = Book.builder()
                .setAuthorId(authorId)
                .setGenreId(genreId)
                .setTitle("MOby Dick")
                .setDescription("Lmao")
                .setPublishDate(LocalDate.now());

        Book book1 = builder
                .setId(UUID.randomUUID())
                .build();
        Book book2 = builder
                .setId(UUID.randomUUID())
                .build();
        Book book3 = builder
                .setId(UUID.randomUUID())
                .build();

        AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
        authorService.add(new Author(authorId, "Ionel Gamer", new HashSet<>()));
        manager.addBook(book1, genreId, authorId, 100, 20.40);
        manager.addBook(book2, genreId, authorId, 100, 20.40);
        manager.addBook(book3, genreId, authorId, 100, 20.40);
        System.out.println("Authors");
        authorService.getItems().forEach((key, item) -> System.out.println(key + " " + item));
        launch();
    }

    public static void configureServices() {
        ServiceRegistry serviceRegistry = ServiceRegistry.getInstance();

        serviceRegistry.register(
                AuthorService.class,
                new AuthorService(new BinaryFileHandler<>(FilePathConfig.AUTHORS_PATH)));

        serviceRegistry.register(
                BookService.class,
                new BookService(new BookFileHandler(FilePathConfig.BOOKS_PATH)));

        serviceRegistry.register(
                GenreService.class,
                new GenreService(new BinaryFileHandler<>(FilePathConfig.GENRES_PATH)));

        serviceRegistry.register(
                InventoryService.class,
                new InventoryService(new InventoryFileHandler(FilePathConfig.INVENTORIES_PATH)));

        serviceRegistry.register(
                LoanService.class,
                new LoanService(new LoanFileHandler(FilePathConfig.LOANS_PATH)));

        serviceRegistry.register(
                MemberService.class,
                new MemberService(new BinaryFileHandler<>(FilePathConfig.MEMBERS_PATH)));

        serviceRegistry.loadData();

        BookService bookService = ServiceRegistry.getInstance().getService(BookService.class);
        System.out.println(bookService.getItems());
    }
}