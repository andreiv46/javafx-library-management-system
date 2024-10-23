package org.ardeu.librarymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.ardeu.librarymanagementsystem.entities.author.Author;
import org.ardeu.librarymanagementsystem.entities.book.Book;
import org.ardeu.librarymanagementsystem.fileio.handlers.BinaryFileHandler;
import org.ardeu.librarymanagementsystem.fileio.handlers.BookFileHandler;
import org.ardeu.librarymanagementsystem.services.AuthorService;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        Author author1 = new Author(UUID.randomUUID(), "Ion", null);
        Author author2 = new Author(UUID.randomUUID(), "Maria", null);
        Author author3 = new Author(UUID.randomUUID(), "Daniel", null);

        Map<UUID, Author> authors = new HashMap<>();
        authors.put(author1.getId(), author1);
        authors.put(author2.getId(), author2);
        authors.put(author3.getId(), author3);
        AuthorService authorService = new AuthorService(new BinaryFileHandler<>("./data/authors.bin"));
        authorService.load();
        launch();

        BookFileHandler bookFileHandler = new BookFileHandler("./data/books.txt");

        List<Book> bookList = new ArrayList<>();

        bookList.add(Book.builder()
                .setId(UUID.randomUUID())
                .setTitle("Book One")
                .setDescription("Description for Book One")
                .setPublishDate(LocalDate.of(2021, 6, 15))
                .setAuthorId(UUID.randomUUID())
                .setGenreId(UUID.randomUUID())
                .build());

        bookList.add(Book.builder()
                .setId(UUID.randomUUID())
                .setTitle("Book Two")
                .setDescription("Description for Book Two")
                .setPublishDate(LocalDate.of(2019, 9, 20))
                .setAuthorId(UUID.randomUUID())
                .setGenreId(UUID.randomUUID())
                .build());

        bookList.add(Book.builder()
                .setId(UUID.randomUUID())
                .setTitle("Book Three")
                .setDescription("Description for Book Three")
                .setPublishDate(LocalDate.of(2023, 1, 10))
                .setAuthorId(UUID.randomUUID())
                .setGenreId(UUID.randomUUID())
                .build());

        bookFileHandler.writeToFile(bookList);

        List<Book> readBookList = bookFileHandler.readFromFile();
        readBookList.forEach(System.out::println);
    }
}