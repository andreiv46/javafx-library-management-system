package org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.LoanController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreExportField;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * GenreViewController is responsible for managing the UI and logic for viewing genres.
 */
public class GenreViewController {
    private final GenreController genreController;
    private final LoanController loanController;
    private final BookController bookController;
    private ScreenViewController screenViewController;
    private final ObservableMap<UUID, Genre> genres;
    private final ObservableList<Genre> genresList;
    private final FilteredList<Genre> filteredGenresList;
    private final SortedList<Genre> sortedGenresList;
    private final ObservableMap<UUID, Loan> loans;
    private final ObservableMap<UUID, Book> books;


    @FXML
    public MFXTextField genreInput;

    @FXML
    public TableView<Genre> genresTable;

    @FXML
    public CheckComboBox<GenreExportField> exportGenreFieldsTv;

    @FXML
    public Button exportBtn;

    /**
     * Constructs a GenreViewController and initializes the necessary data structures.
     */
    public GenreViewController() {
        genreController = new GenreController();

        genres = genreController.getAllGenres().getData();
        genresList = FXCollections.observableArrayList(genres.values().stream().toList());
        filteredGenresList = new FilteredList<>(genresList, _ -> true);
        sortedGenresList = new SortedList<>(filteredGenresList);

        genres.addListener((MapChangeListener<UUID, Genre>) change -> {
            if (change.wasAdded()) {
                genresList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                genresList.remove(change.getValueRemoved());
            }
            genresTable.refresh();
        });
        loanController = new LoanController();
        loans = loanController.getAllLoans().getData();
        loans.addListener((MapChangeListener<UUID, Loan>) _ -> {
            genresTable.refresh();
        });
        bookController = new BookController();
        books = bookController.getAllBooks().getData();
        books.addListener((MapChangeListener<UUID, Book>) _ -> {
            genresTable.refresh();
        });
    }

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        genresTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedGenresList.comparatorProperty().bind(genresTable.comparatorProperty());
        setUpGenresTable(sortedGenresList);
        genreInput.textProperty().addListener((_, _, _) -> filterGenres());
        setUpGenresFieldsTv();
        exportBtn.setOnAction(_ -> exportGenres());
    }

    /**
     * Exports the genres to a CSV file.
     */
    private void exportGenres() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Export Genres to CSV");
        fileChooser.setInitialFileName("genres.csv");
        File file = fileChooser.showSaveDialog(exportBtn.getScene().getWindow());
        if (file != null) {
            List<GenreExportField> checkModel = exportGenreFieldsTv.getCheckModel().getCheckedItems();

            if (checkModel.isEmpty()) {
                showErrorMessage("Error", "Please select at least one field to export");
                return;
            }

            Result<Void> result = this.genreController.exportGenresToCSV(file, sortedGenresList, checkModel);
            if(result.isSuccess()){
                showSuccessMessage("Success", "Genres exported successfully");
            } else {
                showErrorMessage("Error", result.getErrorMessage());
            }
        }
    }

    /**
     * Sets up the export genre fields CheckComboBox.
     */
    private void setUpGenresFieldsTv() {
        exportGenreFieldsTv.getItems().addAll(GenreExportField.values());
        exportGenreFieldsTv.setConverter(new StringConverter<GenreExportField>() {
            @Override
            public String toString(GenreExportField genreExportField) {
                return genreExportField.getDisplayName();
            }

            @Override
            public GenreExportField fromString(String s) {
                return GenreExportField.valueOf(s);
            }
        });
        exportGenreFieldsTv.setTitle("Fields to export");
        exportGenreFieldsTv.getCheckModel().checkAll();
    }

    /**
     * Filters the genres based on the input field.
     */
    private void filterGenres() {
        filteredGenresList.setPredicate(genre -> {
            String genreName = genreInput.getText().toLowerCase();
            return genre.getName().toLowerCase().contains(genreName);
        });
    }

    /**
     * Sets up the genres table with the specified sorted list.
     *
     * @param sortedGenresList the sorted list of genres
     */
    private void setUpGenresTable(SortedList<Genre> sortedGenresList) {
        // id column
        TableColumn<Genre, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(70);
        idColumn.setComparator(UUID::compareTo);
        idColumn.setSortable(true);

        // name column
        TableColumn<Genre, String> nameColumn = new TableColumn<>("Genre name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(true);

        // number of books column
        TableColumn<Genre, Integer> numberOfBooksColumn = new TableColumn<>("Nr. of Books");
        numberOfBooksColumn.setCellValueFactory(param -> {
            Genre genre = param.getValue();
            return new SimpleIntegerProperty(genre.getBooks().size()).asObject();
        });
        numberOfBooksColumn.setPrefWidth(50);
        numberOfBooksColumn.setComparator(Integer::compareTo);
        numberOfBooksColumn.setSortable(true);

        // number of loans column
        TableColumn<Genre, Long> numberOfLoansColumn = new TableColumn<>("Nr. of Loans");
        numberOfLoansColumn.setCellValueFactory(param -> {
            Genre genre = param.getValue();
            Result<Long> result = genreController.getLoansCount(genre.getId());
            return new SimpleLongProperty(result.getData().intValue()).asObject();
        });
        genresTable.getColumns().addAll(idColumn, nameColumn, numberOfBooksColumn, numberOfLoansColumn);
        genresTable.setItems(sortedGenresList);
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

    /**
     * Updates the genres table.
     */
    public void updateTable() {
        genresTable.refresh();
    }
}
