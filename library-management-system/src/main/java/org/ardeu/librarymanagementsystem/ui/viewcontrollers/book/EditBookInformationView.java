package org.ardeu.librarymanagementsystem.ui.viewcontrollers.book;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.util.StringConverter;
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;

import java.util.UUID;
import java.util.stream.Collectors;

public class EditBookInformationView {

    @FXML
    public MFXTextField titleInput;

    @FXML
    public TextArea descriptionInput;

    @FXML
    public Button saveBtn;

    @FXML
    public Button cancelBtn;

    @FXML
    ComboBox<Author> authorComboBox;

    @FXML
    ComboBox<Genre> genreComboBox;

    private Book book;
    private ObservableList<Author> authors;
    private ObservableList<Genre> genres;
    private final BookController bookController = new BookController();
    private final AuthorController authorController = new AuthorController();
    private final GenreController genreController = new GenreController();

    @FXML
    public void initialize() {
        initializeAuthorComboBox();
        initializeGenreComboBox();
    }

    private void initializeGenreComboBox() {
        Result<ObservableMap<UUID, Genre>> genresResult = genreController.getAllGenres();
        if (genresResult.isSuccess()) {
            genres = genresResult.getData()
                    .values()
                    .stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            StringConverter<Genre> converter =
                    FunctionalStringConverter.to(genre -> (genre == null) ? "" : genre.getName());
            genreComboBox.setConverter(converter);
            genreComboBox.setItems(genres);
            genreComboBox.setVisibleRowCount(4);
        }
    }

    private void initializeAuthorComboBox() {
        Result<ObservableMap<UUID, Author>> authorsResult = authorController.getAllAuthors();
        if (authorsResult.isSuccess()) {
            authors = authorsResult.getData()
                    .values()
                    .stream()
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            StringConverter<Author> converter =
                    FunctionalStringConverter.to(author -> (author == null) ? "" : author.getName());
            authorComboBox.setConverter(converter);
            authorComboBox.setItems(authors);
            authorComboBox.setVisibleRowCount(4);
        }
    }

    public void setBook(Book book) {
        titleInput.setText(book.getTitle());
        descriptionInput.setText(book.getDescription());
        authors.stream()
                .filter(author -> author.getId().equals(book.getAuthorId()))
                .findFirst()
                .ifPresent(authorComboBox::setValue);
        genres.stream()
                .filter(genre -> genre.getId().equals(book.getGenreId()))
                .findFirst()
                .ifPresent(genreComboBox::setValue);
    }

    public void onSaveBtnClick(ActionEvent actionEvent) {
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        Author author = authorComboBox.getValue();
        Genre genre = genreComboBox.getValue();
        if (title.isEmpty() || description.isEmpty() || author == null || genre == null) {
            return;
        }
        System.out.println("Saving book");
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Author: " + author.getName());
        System.out.println("Genre: " + genre.getName());
    }

    public void onCancelBtnClick(ActionEvent actionEvent) {
    }
}
