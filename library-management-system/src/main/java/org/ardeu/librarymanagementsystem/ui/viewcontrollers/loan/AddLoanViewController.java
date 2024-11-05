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
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanDTO;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.controlsfx.control.ListActionView;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.glyphfont.FontAwesome;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

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

    public void updateBooksList() {
        clearFields();
        List<Book> availableBooks = this.inventoryController.getAvailableBooks().getData();
        this.booksList.clear();
        this.booksList.addAll(availableBooks);
    }

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

    private void initializeSourceInputFilter() {
        this.sourceInputFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            this.sourceLv.getItems().clear();
            this.sourceLv.getItems()
                    .addAll(this.filteredBooks.filtered(book ->
                            book.getTitle().toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT))));
        });
    }

    private void initializeTargetInputFilter() {
        this.targetInputFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            this.targetLv.getItems().clear();
            this.targetLv.getItems()
                    .addAll(this.selectedBooks.filtered(book ->
                            book.getTitle().toLowerCase(Locale.ROOT).contains(newValue.toLowerCase(Locale.ROOT))));
        });
    }

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

    private void addBookToTargetLv(Book selectedBook) {
        if(selectedBooks.contains(selectedBook)){
            showErrorMessage("Error", "Book is already selected.");
            return;
        }
        selectedBooks.add(selectedBook);
        targetLv.getItems().clear();
        targetLv.getItems().addAll(selectedBooks);
    }

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

        List<LoanDTO> loanDTOS = selectedBooks.stream()
                .map(book -> {
                    Result<Inventory> inventoryResult = inventoryController.getInventoryByBookId(book.getId());

                    if(!inventoryResult.isSuccess()){
                        showErrorMessage("Error", inventoryResult.getErrorMessage());
                        return null;
                    }

                    return new LoanDTO(
                            book.getId(),
                            member.getId(),
                            dueDate,
                            inventoryResult.getData().getPrice()
                    );
                })
                .toList();

        Result<List<Loan>> loansResult = this.loanController.addLoansForUser(loanDTOS, member.getId());
        if(loansResult.isSuccess()){
            showSuccessMessage("Success", "Loan added successfully.");
            clearFields();
            screenViewController.activate(ScreenName.LOANS);
        } else {
            showErrorMessage("Error", loansResult.getErrorMessage());
        }
        clearFields();
        screenViewController.activate(ScreenName.LOANS);
    }

    private void clearFields() {
        memberComboBoxInput.setValue(null);
        dueDatePicker.setValue(null);
        selectedBooks.clear();
        targetLv.getItems().clear();
    }

    private void onCancel() {
        clearFields();
        screenViewController.activate(ScreenName.LOANS);
    }

    private void initializeMemberComboBox() {
        StringConverter<Member> memberStringConverter =
                FunctionalStringConverter.to(member -> (Objects.isNull(member)) ? "" :
                        member.getEmail() + " - " + member.getName());
        memberComboBoxInput.setConverter(memberStringConverter);
        memberComboBoxInput.setItems(this.membersList);
    }

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }

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

    private void showErrorMessage(String message, String content){
        ErrorAlert errorAlert = new ErrorAlert(message);
        errorAlert.setContent(content);
        errorAlert.showAlert();
    }

    private void showSuccessMessage(String message, String content){
        SuccessAlert successAlert = new SuccessAlert(message);
        successAlert.setContent(content);
        successAlert.showAlert();
    }
}
