package org.ardeu.librarymanagementsystem.ui.viewcontrollers.book;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.InventoryController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

public class BookDetailsViewController {

    @FXML
    public Label bookIdLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Label authorLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label publishDateLabel;
    @FXML
    private Label availableCopiesLabel;
    @FXML
    private Label totalCopiesLabel;
    @FXML
    private Label priceLabel;

    private final AuthorController authorController = new AuthorController();
    private final GenreController genreController = new GenreController();
    private final InventoryController inventoryController = new InventoryController();
    private ScreenViewController screenViewController;
    private Book book;

    public void setBook(Book book) {
        this.book = book;
        bookIdLabel.setText(String.valueOf(book.getId()));
        titleLabel.setText(book.getTitle());
        descriptionTextArea.setText(book.getDescription());
        Result<Author> authorResult = authorController.getAuthorById(book.getAuthorId());
        if (authorResult.isSuccess()) {
            authorLabel.setText(authorResult.getData().getName());
        }
        else {
            authorLabel.setText("Unknown");
        }

        Result<Genre> genreResult = genreController.getGenreById(book.getGenreId());
        if (genreResult.isSuccess()) {
            genreLabel.setText(genreResult.getData().getName());
        }
        else {
            genreLabel.setText("Unknown");
        }

        publishDateLabel.setText(book.getPublishDate().toString());

        Result<Inventory> inventoryResult = inventoryController.getInventoryByBookId(book.getId());
        if (inventoryResult.isSuccess()) {
            availableCopiesLabel.setText(String.valueOf(inventoryResult.getData().getAvailableCopies()));
            totalCopiesLabel.setText(String.valueOf(inventoryResult.getData().getTotalCopies()));
            priceLabel.setText(String.valueOf(inventoryResult.getData().getPrice()));
        }
        else {
            availableCopiesLabel.setText("0");
            totalCopiesLabel.setText("0");
            priceLabel.setText("0.0");
        }
    }

    public void handleEditButtonClick() {
        handleEditBook();
    }

    private void handleEditBook() {
        EditBookInformationView editBookInformationView =
                (EditBookInformationView) screenViewController.getController(ScreenName.EDIT_BOOK_INFORMATION);
        editBookInformationView.setBook(book);
        screenViewController.activate(ScreenName.EDIT_BOOK_INFORMATION);
    }

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }
}