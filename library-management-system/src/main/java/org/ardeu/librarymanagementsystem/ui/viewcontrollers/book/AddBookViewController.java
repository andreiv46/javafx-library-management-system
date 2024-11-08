package org.ardeu.librarymanagementsystem.ui.viewcontrollers.book;

import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.BookCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.author.AuthorsViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre.GenreViewController;
import org.controlsfx.control.SearchableComboBox;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Controller for the Add Book screen
 */
public class AddBookViewController {

    private ScreenViewController screenViewController;

    private final BookController bookController;
    private final AuthorController authorController;
    private final GenreController genreController;

    private final ObservableMap<UUID, Author> authors;
    private final ObservableMap<UUID, Genre> genres;

    private final ObservableList<Author> authorsList;
    private final ObservableList<Genre> genresList;

    @FXML
    public TextField titleInput;

    @FXML
    public Button addBookBtn;

    @FXML
    public Button cancelBtn;

    @FXML
    public SearchableComboBox<Author> authorInput;

    @FXML
    public SearchableComboBox<Genre> genreInput;

    @FXML
    public GridPane gridPane;

    @FXML
    public VBox rootVBox;

    @FXML
    public TextArea descriptionInput;

    @FXML
    public DatePicker dateInput;

    @FXML
    public TextField totalCopiesInput;

    @FXML
    public TextField priceInput;

    /**
     * Constructs an AddBookViewController and initializes the necessary data structures.
     */
    public AddBookViewController() {
        this.bookController = new BookController();
        this.authorController = new AuthorController();
        this.genreController = new GenreController();

        this.authors = this.authorController.getAllAuthors().getData();
        this.genres = this.genreController.getAllGenres().getData();

        this.authorsList = FXCollections.observableArrayList(authors.values());
        this.genresList = FXCollections.observableArrayList(genres.values());

        this.authors.addListener((MapChangeListener<UUID, Author>) change -> {
            Platform.runLater(() -> {
                if (change.wasAdded()) {
                    authorsList.add(change.getValueAdded());
                } else if (change.wasRemoved()) {
                    authorsList.remove(change.getValueRemoved());
                }
            });
        });

        this.genres.addListener((MapChangeListener<UUID, Genre>) change -> {
            Platform.runLater(() -> {
                if (change.wasAdded()) {
                    genresList.add(change.getValueAdded());
                } else if (change.wasRemoved()) {
                    genresList.remove(change.getValueRemoved());
                }
            });
        });
    }

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        initializeAuthorComboBox();
        initializeGenreComboBox();
        initializeDescriptionInput();
        initializeDateInput();
        initializeTotalCopiesInput();
        initializePriceInput();
        this.addBookBtn.setOnAction(_ -> addBook());
        this.cancelBtn.setOnAction(_ -> screenViewController.activate(ScreenName.BOOKS));
    }

    /**
     * Initializes the price input field with validation.
     */
    private void initializePriceInput() {
        priceInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceInput.setText(newValue.replaceAll("[^\\d.]", ""));

                if (newValue.indexOf('.') != newValue.lastIndexOf('.')) {
                    priceInput.setText(newValue.replaceAll("\\.(?=.*\\.)", ""));
                }
            }
        });
    }

    /**
     * Initializes the total copies input field with validation.
     */
    private void initializeTotalCopiesInput() {
        totalCopiesInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                totalCopiesInput.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Initializes the date input field with a prompt text and default value.
     */
    private void initializeDateInput() {
        dateInput.setPromptText("Publication Date");
        dateInput.setValue(LocalDate.now());
    }

    /**
     * Initializes the description input field with wrap text enabled.
     */
    private void initializeDescriptionInput() {
        descriptionInput.setWrapText(true);
    }

    /**
     * Adds a new book using the provided input data.
     */
    private void addBook() {
        String title = titleInput.getText();
        String description = descriptionInput.getText();
        Genre genre = genreInput.getValue();
        Author author = authorInput.getValue();
        LocalDate releaseDate = dateInput.getValue();

        int totalCopies;
        double price;

        try {
            totalCopies = Integer.parseInt(totalCopiesInput.getText());
            price = Double.parseDouble(priceInput.getText());
        }
        catch (NumberFormatException e) {
            showErrorMessage("Invalid input", "Please enter a valid number for total copies and price");
            return;
        }

        if (title.isEmpty() || description.isEmpty() || genre == null || author == null) {
            showErrorMessage("Invalid input", "Please fill all the fields");
            return;
        }

        BookCreationDTO bookDTO = new BookCreationDTO(title, description, author.getId(), genre.getId(), releaseDate);
        Result<Book> result = this.bookController.addBook(bookDTO, totalCopies, price);
        if(result.isSuccess()){
            showSuccessMessage(
                    "Book added", "Book with title: " +
                    result.getData().getTitle() + " added successfully");
            clearFields();
            updateData();
            screenViewController.activate(ScreenName.BOOKS);
        } else {
            showErrorMessage("Couldn't add book", result.getErrorMessage());
        }
    }

    /**
     * Updates the data in the authors and genres tables.
     */
    private void updateData() {
        AuthorsViewController authorsViewController = (AuthorsViewController) screenViewController.getController(ScreenName.AUTHORS);
        GenreViewController genreViewController = (GenreViewController) screenViewController.getController(ScreenName.GENRES);

        authorsViewController.updateTable();
        genreViewController.updateTable();
    }

    /**
     * Clears all input fields.
     */
    private void clearFields() {
        titleInput.clear();
        descriptionInput.clear();
        authorInput.setValue(null);
        genreInput.setValue(null);
        dateInput.setValue(LocalDate.now());
        totalCopiesInput.clear();
        priceInput.clear();
    }

    /**
     * Initializes the author combo box with a string converter.
     */
    private void initializeAuthorComboBox() {
        StringConverter<Author> authorStringConverter =
                FunctionalStringConverter.to(author -> (author == null) ? "" : author.getName());
        this.authorInput.setConverter(authorStringConverter);
        this.authorInput.setItems(this.authorsList);
    }

    /**
     * Initializes the genre combo box with a string converter.
     */
    private void initializeGenreComboBox() {
        StringConverter<Genre> genreStringConverter =
                FunctionalStringConverter.to(genre -> (genre == null) ? "" : genre.getName());
        this.genreInput.setConverter(genreStringConverter);
        this.genreInput.setItems(this.genresList);
    }

    /**
     * Sets the ScreenViewController for this controller.
     *
     * @param screenViewController the ScreenViewController to set
     */
    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }

    /**
     * Displays an error message using an ErrorAlert.
     *
     * @param message the title of the error message
     * @param content the content of the error message
     */
    private void showErrorMessage(String message, String content){
        ErrorAlert errorAlert = new ErrorAlert(message);
        errorAlert.setContent(content);
        errorAlert.showAlert();
    }

    /**
     * Displays a success message using a SuccessAlert.
     *
     * @param message the title of the success message
     * @param content the content of the success message
     */
    private void showSuccessMessage(String message, String content){
        SuccessAlert successAlert = new SuccessAlert(message);
        successAlert.setContent(content);
        successAlert.showAlert();
    }
}
