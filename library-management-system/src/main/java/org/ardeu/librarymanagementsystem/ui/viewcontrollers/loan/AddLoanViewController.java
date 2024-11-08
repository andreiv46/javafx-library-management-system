package org.ardeu.librarymanagementsystem.ui.viewcontrollers.loan;

import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.ardeu.librarymanagementsystem.domain.controllers.*;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.inventory.Inventory;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanCreationDTO;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.author.AuthorsViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre.GenreViewController;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.member.MembersViewController;
import org.controlsfx.control.ListActionView;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * AddLoanViewController is responsible for managing the UI and logic for adding a new loan.
 */
public class AddLoanViewController {
    private ScreenViewController screenViewController;
    private final MemberController memberController;
    private final BookController bookController;
    private final InventoryController inventoryController;
    private final AuthorController authorController;
    private final LoanController loanController;

    private final ObservableMap<UUID, Member> members;
    private final ObservableList<Member> membersList;
    private final ObservableMap<UUID, Book> books;
    private final ObservableList<Book> booksList;
    private final ObservableList<Book> selectedBooks;
    private final FilteredList<Book> filteredBooks;
    private final ObservableMap<UUID, Inventory> inventories;

    @FXML
    public Button addLoanBtn;

    @FXML
    public Button cancelBtn;

    @FXML
    public SearchableComboBox<Member> memberComboBoxInput;

    @FXML
    public ListActionView<Book> sourceLv;

    @FXML
    public ListActionView<Book> targetLv;

    @FXML
    public TextField sourceInputFilter;

    @FXML
    public TextField targetInputFilter;

    @FXML
    public DatePicker dueDatePicker;

    /**
     * Constructs an AddLoanViewController and initializes the necessary data structures.
     */
    public AddLoanViewController() {
        this.memberController = new MemberController();
        this.members = this.memberController.getAllMembers().getData();
        this.membersList = FXCollections.observableArrayList(this.members.values());
        this.members.addListener((MapChangeListener<UUID, Member>) change -> {
            if (change.wasAdded()) {
                this.membersList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                this.membersList.remove(change.getValueRemoved());
            }
        });

        this.inventoryController = new InventoryController();

        this.bookController = new BookController();
        this.books = this.bookController.getAllBooks().getData();
        List<Book> availableBooks = this.inventoryController.getAvailableBooks().getData();
        this.booksList = FXCollections.observableArrayList(availableBooks);
        this.books.addListener((MapChangeListener<UUID, Book>) change -> {
            if(change.wasAdded() || change.wasRemoved()){
                updateBooksList();
            }
        });
        this.selectedBooks = FXCollections.observableArrayList();
        this.filteredBooks = new FilteredList<>(this.booksList, _ -> true);

        this.authorController = new AuthorController();
        this.inventories = this.inventoryController.getAllInventories().getData();
        this.inventories.addListener((MapChangeListener<UUID, Inventory>) change -> {
            if (change.wasAdded()) {
                updateBooksList();
            } else if (change.wasRemoved()) {
                updateBooksList();
            }
        });
        loanController = new LoanController();
    }

    /**
     * Updates the list of available books.
     */
    public void updateBooksList() {
        clearFields();
        List<Book> availableBooks = this.inventoryController.getAvailableBooks().getData();
        this.booksList.clear();
        this.booksList.addAll(availableBooks);
        this.sourceLv.getItems().addAll(this.filteredBooks);
    }

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        initializeMemberComboBox();
        initializeSourceLv();
        initializeTargetLv();
        initializeSourceInputFilter();
        initializeTargetInputFilter();

        cancelBtn.setOnAction(_ -> onCancel());
        addLoanBtn.setOnAction(_ -> addLoan());
    }

    /**
     * Initializes the source input filter.
     */
    private void initializeSourceInputFilter() {
        this.sourceInputFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            this.sourceLv.getItems().clear();
            this.sourceLv.getItems()
                    .addAll(this.filteredBooks.filtered(book ->
                            book.getTitle().toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT))));
        });
    }

    /**
     * Initializes the target input filter.
     */
    private void initializeTargetInputFilter() {
        this.targetInputFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            this.targetLv.getItems().clear();
            this.targetLv.getItems()
                    .addAll(this.selectedBooks.filtered(book ->
                            book.getTitle().toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT))));
        });
    }

    /**
     * Initializes the target ListActionView.
     */
    private void initializeTargetLv() {
        this.targetLv.getItems().addAll(this.selectedBooks);
        this.targetLv.setCellFactory(this::createLvCell);
        this.targetLv
                .getActions()
                .add(new ListActionView.ListAction<Book>(new FontAwesome().create(FontAwesome.Glyph.MINUS)) {
                    @Override
                    public void initialize(ListView<Book> listView) {
                        setEventHandler(_ -> {
                            Book selectedBook = listView.getSelectionModel().getSelectedItem();
                            if (Objects.nonNull(selectedBook)) {
                                selectedBooks.remove(selectedBook);
                                targetLv.getItems().clear();
                                targetLv.getItems().addAll(selectedBooks);
                            }
                        });
                    }
                });
    }

    /**
     * Initializes the source ListActionView.
     */
    private void initializeSourceLv() {
        this.sourceLv.getItems().addAll(this.filteredBooks);
        this.sourceLv.setCellFactory(this::createLvCell);
        this.sourceLv
                .getActions()
                .add(new ListActionView.ListAction<Book>(new FontAwesome().create(FontAwesome.Glyph.PLUS)) {
                    @Override
                    public void initialize(ListView<Book> listView) {
                        setEventHandler(_ -> {
                            Book selectedBook = listView.getSelectionModel().getSelectedItem();
                            if (Objects.nonNull(selectedBook)) {
                                addBookToTargetLv(selectedBook);
                            }
                        });
                    }
                });
        this.sourceLv.sideProperty().setValue(Side.RIGHT);
    }

    /**
     * Adds a book to the target ListActionView.
     *
     * @param selectedBook the book to add
     */
    private void addBookToTargetLv(Book selectedBook) {
        if(selectedBooks.contains(selectedBook)){
            showErrorMessage("Error", "Book is already selected.");
            return;
        }
        selectedBooks.add(selectedBook);
        targetLv.getItems().clear();
        targetLv.getItems().addAll(selectedBooks);
    }

    /**
     * Adds a new loan using the provided input data.
     */
    private void addLoan() {
        Member member = memberComboBoxInput.getValue();

        if (Objects.isNull(member)) {
            showErrorMessage("Error", "Please select a member.");
            return;
        }

        if (selectedBooks.isEmpty()) {
            showErrorMessage("Error", "Please select at least one book.");
            return;
        }

        LocalDate dueDate = dueDatePicker.getValue();
        if(Objects.isNull(dueDate)){
            showErrorMessage("Error", "Please select a due date.");
            return;
        }

        List<LoanCreationDTO> loanDTOS = selectedBooks.stream()
                .map(book -> {
                    Result<Inventory> inventoryResult = inventoryController.getInventoryByBookId(book.getId());

                    if(!inventoryResult.isSuccess()){
                        showErrorMessage("Error", inventoryResult.getErrorMessage());
                        return null;
                    }

                    return new LoanCreationDTO(
                            book.getId(),
                            member.getId(),
                            dueDate,
                            inventoryResult.getData().getPrice()
                    );
                })
                .toList();

        Result<List<Loan>> loansResult = this.loanController.addLoansForUser(loanDTOS, member.getId());
        if(loansResult.isSuccess()){
            updateTables();
            showSuccessMessage("Success", "Loan added successfully.");
            clearFields();
            screenViewController.activate(ScreenName.LOANS);
        } else {
            showErrorMessage("Error", loansResult.getErrorMessage());
        }
        clearFields();
        screenViewController.activate(ScreenName.LOANS);
    }

    /**
     * Updates the tables in the related view controllers.
     */
    private void updateTables() {
        GenreViewController genreViewController = (GenreViewController) screenViewController.getController(ScreenName.GENRES);
        AuthorsViewController authorsViewController = (AuthorsViewController) screenViewController.getController(ScreenName.AUTHORS);
        MembersViewController membersViewController = (MembersViewController) screenViewController.getController(ScreenName.MEMBERS);
        genreViewController.updateTable();
        authorsViewController.updateTable();
        membersViewController.updateTable();
    }

    /**
     * Clears the input fields.
     */
    private void clearFields() {
        memberComboBoxInput.setValue(null);
        dueDatePicker.setValue(null);
        selectedBooks.clear();
        targetLv.getItems().clear();
        sourceLv.getItems().clear();
    }

    /**
     * Handles the cancel action, clearing input fields and navigating back to the loans screen.
     */
    private void onCancel() {
        clearFields();
        screenViewController.activate(ScreenName.LOANS);
    }

    /**
     * Initializes the member combo box.
     */
    private void initializeMemberComboBox() {
        StringConverter<Member> memberStringConverter =
                FunctionalStringConverter.to(member -> (Objects.isNull(member)) ? "" :
                        member.getEmail() + " - " + member.getName());
        memberComboBoxInput.setConverter(memberStringConverter);
        memberComboBoxInput.setItems(this.membersList);
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
     * Creates a ListCell for the ListView.
     *
     * @param param the ListView parameter
     * @return a ListCell for the ListView
     */
    private ListCell<Book> createLvCell(ListView<Book> param) {
        return new ListCell<>() {
            @Override
            protected void updateItem(Book item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || Objects.isNull(item)) {
                    setText(null);
                } else {
                    Result<Author> authorResult = authorController.getAuthorById(item.getAuthorId());
                    setText(item.getTitle() + " by " + authorResult.getData().getName() + " - " + item.getPublishDate());
                }
            }
        };
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
