package org.ardeu.librarymanagementsystem.ui.viewcontrollers.author;

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
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.LoanController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorExportField;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * AuthorsViewController is responsible for managing the UI and logic for viewing authors.
 */
public class AuthorsViewController {

    private ScreenViewController screenViewController;
    private final AuthorController authorController;
    private final BookController bookController;
    private final ObservableMap<UUID, Author> authors;
    private final ObservableList<Author> authorsList;
    private final FilteredList<Author> filteredAuthorsList;
    private final SortedList<Author> sortedAuthorsList;
    private final ObservableMap<UUID, Loan> loans;
    private final LoanController loanController;
    private final ObservableMap<UUID, Book> books;

    @FXML
    public MFXTextField authorInput;

    @FXML
    public TableView<Author> authorsTable;

    @FXML
    public CheckComboBox<AuthorExportField> exportAuthorsFieldsTv;

    @FXML
    public Button exportBtn;

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize(){
        authorsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedAuthorsList.comparatorProperty().bind(authorsTable.comparatorProperty());
        setUpAuthorsTable(sortedAuthorsList);
        authorInput.textProperty().addListener((_, _, _) -> filterAuthors());
        setUpAuthorsExportFieldsTv();
        exportBtn.setOnAction(_ -> exportAuthors());
    }

    /**
     * Exports the authors to a CSV file.
     */
    private void exportAuthors() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Export Authors to CSV");
        fileChooser.setInitialFileName("authors.csv");
        File file = fileChooser.showSaveDialog(exportBtn.getScene().getWindow());

        if (file != null) {
            List<AuthorExportField> checkModel = exportAuthorsFieldsTv.getCheckModel().getCheckedItems();

            if (checkModel.isEmpty()) {
                showErrorMessage("Error", "Please select at least one field to export");
                return;
            }
            Result<Void> result = this.authorController.exportAuthorsToCSV(file, sortedAuthorsList, checkModel);
            if(result.isSuccess()){
                showSuccessMessage("Success", "Authors exported successfully");
            } else {
                showErrorMessage("Error", result.getErrorMessage());
            }
        }
    }

    /**
     * Sets up the export authors fields CheckComboBox.
     */
    private void setUpAuthorsExportFieldsTv() {
        exportAuthorsFieldsTv.getItems().addAll(AuthorExportField.values());
        exportAuthorsFieldsTv.setConverter(new StringConverter<>() {
            @Override
            public String toString(AuthorExportField authorExportField) {
                return authorExportField.getDisplayName();
            }

            @Override
            public AuthorExportField fromString(String s) {
                return AuthorExportField.valueOf(s);
            }
        });
        exportAuthorsFieldsTv.setTitle("Fields to Export");
        exportAuthorsFieldsTv.getCheckModel().checkAll();
    }

    /**
     * Filters the authors based on the input field.
     */
    private void filterAuthors() {
        filteredAuthorsList.setPredicate(author -> {
            String authorName = authorInput.getText().toLowerCase();
            return author.getName().toLowerCase().contains(authorName);
        });
    }

    /**
     * Sets up the authors table with the specified sorted list.
     *
     * @param sortedAuthorsList the sorted list of authors
     */
    private void setUpAuthorsTable(SortedList<Author> sortedAuthorsList) {
        // id column
        TableColumn<Author, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(70);
        idColumn.setComparator(UUID::compareTo);
        idColumn.setSortable(true);

        // name column
        TableColumn<Author, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setSortable(true);
        nameColumn.setComparator(String::compareTo);

        // number of books column
        TableColumn<Author, Integer> numberOfBooksColumn = new TableColumn<>("Nr. of Books");
        numberOfBooksColumn.setCellValueFactory(param -> {
            Author author = param.getValue();
            return new SimpleIntegerProperty(author.getBooks().size()).asObject();
        });
        numberOfBooksColumn.setPrefWidth(150);
        numberOfBooksColumn.setSortable(true);
        numberOfBooksColumn.setComparator(Integer::compareTo);

        // number of loans column
        TableColumn<Author, Long> numberOfLoansColumn = new TableColumn<>("Nr. of Loans");
        numberOfLoansColumn.setCellValueFactory(param -> {
            Author genre = param.getValue();
            Result<Long> result = authorController.getLoansCount(genre.getId());
            return new SimpleLongProperty(result.getData().intValue()).asObject();
        });

        authorsTable.getColumns().addAll(idColumn, nameColumn, numberOfBooksColumn, numberOfLoansColumn);
        authorsTable.setItems(sortedAuthorsList);
    }

    /**
     * Constructs an AuthorsViewController and initializes the necessary data structures.
     */
    public AuthorsViewController() {
        authorController = new AuthorController();
        loanController = new LoanController();

        authors = authorController.getAllAuthors().getData();
        authorsList = FXCollections.observableArrayList(authors.values().stream().toList());
        filteredAuthorsList = new FilteredList<>(authorsList, _ -> true);
        sortedAuthorsList = new SortedList<>(filteredAuthorsList);

        authors.addListener((MapChangeListener<UUID, Author>) change -> {
            if (change.wasAdded()) {
                authorsList.add(change.getValueAdded());
            }
            if (change.wasRemoved()) {
                authorsList.remove(change.getValueRemoved());
            }
            authorsTable.refresh();
        });
        loans = loanController.getAllLoans().getData();
        loans.addListener((MapChangeListener<UUID, Loan>) _ -> {
            authorsTable.refresh();
        });

        bookController = new BookController();

        books = bookController.getAllBooks().getData();
        books.addListener((MapChangeListener<UUID, Book>) _ -> {
            authorsTable.refresh();
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

    /**
     * Updates the authors table.
     */
    public void updateTable() {
        authorsTable.refresh();
    }
}
