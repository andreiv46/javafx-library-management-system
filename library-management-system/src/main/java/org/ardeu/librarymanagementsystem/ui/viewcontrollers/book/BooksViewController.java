package org.ardeu.librarymanagementsystem.ui.viewcontrollers.book;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.book.mappers.BookExportField;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

/**
 * The controller for the BooksView.
 */
public class BooksViewController {

    private ScreenViewController screenViewController;
    private final BookController bookController;
    private final AuthorController authorController;
    private final GenreController genreController;
    private final ObservableMap<UUID, Book> books;
    private final ObservableList<Book> bookList;
    private final FilteredList<Book> filteredBookList;
    private final SortedList<Book> sortedBookList;

    @FXML
    public TableView<Book> booksTable;

    @FXML
    public MFXTextField titleInput;

    @FXML
    public MFXTextField authorInput;

    @FXML
    public MFXTextField genreInput;

    @FXML
    public Button exportBtn;

    @FXML
    public CheckComboBox<BookExportField> exportBookFieldsTv;

    /**
     * Constructs a BooksViewController and initializes the necessary data structures.
     */
    public BooksViewController() {
        bookController = new BookController();
        authorController = new AuthorController();
        genreController = new GenreController();

        books = bookController.getAllBooks().getData();
        bookList = FXCollections.observableArrayList(books.values().stream().toList());
        filteredBookList = new FilteredList<>(bookList, _ -> true);
        sortedBookList = new SortedList<>(filteredBookList);

        books.addListener((MapChangeListener<UUID, Book>) change -> {
            if (change.wasAdded()) {
                bookList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                bookList.remove(change.getValueRemoved());
            }
        });
    }

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        booksTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedBookList.comparatorProperty().bind(booksTable.comparatorProperty());
        setUpBooksTable(sortedBookList);
        setUpBookCheckTreeView(exportBookFieldsTv);

        titleInput.textProperty().addListener((_, _, _) -> filterBooks());
        authorInput.textProperty().addListener((_, _, _) -> filterBooks());
        genreInput.textProperty().addListener((_, _, _) -> filterBooks());

        booksTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && booksTable.getSelectionModel().getSelectedItem() != null) {
                Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
                openBookDetails(selectedBook);
            }
        });

        this.exportBtn.setOnAction(_ -> exportToCSV());
    }

    /**
     * Sets up the book check tree view with the specified CheckComboBox.
     *
     * @param exportBookFieldsTv the CheckComboBox to set up
     */
    private void setUpBookCheckTreeView(CheckComboBox<BookExportField> exportBookFieldsTv) {
        exportBookFieldsTv.getItems().addAll(BookExportField.values());
        exportBookFieldsTv.setConverter(new StringConverter<>() {
            @Override
            public String toString(BookExportField bookExportField) {
                return bookExportField.getDisplayName();
            }

            @Override
            public BookExportField fromString(String s) {
                return BookExportField.valueOf(s);
            }
        });
        exportBookFieldsTv.titleProperty().set("Fields to Export");
        exportBookFieldsTv.getCheckModel().checkAll();
    }

    /**
     * Exports the books to a CSV file.
     */
    private void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Export Books to CSV");
        fileChooser.setInitialFileName("books.csv");
        File file = fileChooser.showSaveDialog(exportBtn.getScene().getWindow());
        if (file != null) {
            List<BookExportField> checkModel = exportBookFieldsTv.getCheckModel().getCheckedItems();

            if(checkModel.isEmpty()){
                showErrorMessage("Error", "Please select at least one field to export");
                return;
            }

            Result<Void> result = this.bookController.exportBooksToCSV(file, sortedBookList, checkModel);
            if(result.isSuccess()){
                showSuccessMessage("Success", "Books exported successfully");
            } else {
                showErrorMessage("Error", result.getErrorMessage());
            }
        }
    }

    /**
     * Opens the book details view for the selected book.
     *
     * @param selectedBook the book to display details for
     */
    private void openBookDetails(Book selectedBook) {
        BookDetailsViewController bookDetailsViewController =
                (BookDetailsViewController) screenViewController.getController(ScreenName.BOOK_DETAILS);
        bookDetailsViewController.setBook(selectedBook);
        screenViewController.activate(ScreenName.BOOK_DETAILS);
    }

    /**
     * Sets up the books table with the specified observable list.
     *
     * @param bookList the observable list of books
     */
    private void setUpBooksTable(ObservableList<Book> bookList){
        // id column
        TableColumn<Book, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(70);
        idColumn.setComparator(UUID::compareTo);
        idColumn.setSortable(true);

        // title column
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setComparator(String::compareTo);
        titleColumn.setSortable(true);


        // author column
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> {
            UUID authorId = cellData.getValue().getAuthorId();
            Result<Author> result = authorController.getAuthorById(authorId);
            if(result.isSuccess()){
                Author author = result.getData();
                return new SimpleStringProperty(author.getName());
            }
            return new SimpleStringProperty("not found");
        });
        authorColumn.setComparator(String::compareToIgnoreCase);
        authorColumn.setSortable(true);

        // date column
        TableColumn<Book, LocalDate> dateColumn = new TableColumn<>("Publication Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        dateColumn.setComparator(LocalDate::compareTo);
        dateColumn.setSortable(true);

        // genre column
        TableColumn<Book, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(cellData -> {
            UUID genreId = cellData.getValue().getGenreId();
            Result<Genre> result = genreController.getGenreById(genreId);
            if(result.isSuccess()){
                Genre genre = result.getData();
                return new SimpleStringProperty(genre.getName());
            }
            return new SimpleStringProperty("not found");
        });
        genreColumn.setComparator(String::compareToIgnoreCase);
        genreColumn.setSortable(true);

        booksTable.setSortPolicy(_ -> true);
        booksTable.getColumns().addAll(idColumn, titleColumn, authorColumn, dateColumn, genreColumn);
        booksTable.setItems(bookList);
    }

    /**
     * Filters the books based on the input fields.
     */
    private void filterBooks(){
        filteredBookList.setPredicate(book -> {
            String title = titleInput.getText().toLowerCase(Locale.ROOT);
            String author = authorInput.getText().toLowerCase(Locale.ROOT);
            String genre = genreInput.getText().toLowerCase(Locale.ROOT);

            // author name
            Result<Author> authorResult = authorController.getAuthorById(book.getAuthorId());
            String authorName = "not found";
            if (authorResult.isSuccess()) {
                authorName = authorResult.getData().getName().toLowerCase(Locale.ROOT);
            }

            // genre name
            Result<Genre> genreResult = genreController.getGenreById(book.getGenreId());
            String genreName = "not found";
            if (genreResult.isSuccess()) {
                genreName = genreResult.getData().getName().toLowerCase(Locale.ROOT);
            }

            return book.getTitle().toLowerCase(Locale.ROOT).contains(title) &&
                    authorName.contains(author) &&
                    genreName.contains(genre);
        });
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
