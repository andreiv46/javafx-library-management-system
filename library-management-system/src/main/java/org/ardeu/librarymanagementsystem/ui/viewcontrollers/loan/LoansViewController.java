package org.ardeu.librarymanagementsystem.ui.viewcontrollers.loan;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.LoanController;
import org.ardeu.librarymanagementsystem.domain.controllers.MemberController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanExportField;
import org.ardeu.librarymanagementsystem.domain.entities.loan.LoanStatus;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.MasterDetailPane;
import org.controlsfx.control.PopOver;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

/**
 * LoansViewController is responsible for managing the UI and logic for displaying and exporting loans.
 */
public class LoansViewController {
    private ScreenViewController screenViewController;
    private final LoanController loanController;
    private final BookController bookController;
    private final MemberController memberController;
    private final ObservableMap<UUID, Loan> loans;
    private final ObservableList<Loan> loansList;
    private final FilteredList<Loan> filteredLoansList;
    private final SortedList<Loan> sortedLoansList;
    private final TableView<Loan> loansTable;

    @FXML
    public MFXTextField memberInput;

    @FXML
    public MFXTextField bookInput;

    @FXML
    public MasterDetailPane loansMasterDetailPane;

    @FXML
    public CheckComboBox<LoanExportField> exportLoansFieldsTv;

    @FXML
    public Button exportBtn;

    @FXML
    public MFXTextField statusInput;

    /**
     * Constructs a LoansViewController and initializes the necessary data structures.
     */
    public LoansViewController() {
        this.loanController = new LoanController();
        this.bookController = new BookController();
        this.memberController = new MemberController();

        this.loans = this.loanController.getAllLoans().getData();
        
        this.loansList = FXCollections.observableArrayList(this.loans.values().stream().toList());

        this.loans.addListener((MapChangeListener<UUID, Loan>) loan -> {
            if (loan.wasAdded()) {
                loansList.add(loan.getValueAdded());
            } else if (loan.wasRemoved()) {
                loansList.remove(loan.getValueRemoved());
            }
        });

        this.filteredLoansList = new FilteredList<>(loansList, _ -> true);
        this.sortedLoansList = new SortedList<>(filteredLoansList);
        this.loansTable = new TableView<>();
    }

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        loansTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedLoansList.comparatorProperty().bind(loansTable.comparatorProperty());
        setUpLoansTable(sortedLoansList);
        setUpLoansMasterDetailPane(this.loansTable);
        memberInput.textProperty().addListener((_, _, _) -> filterLoans());
        bookInput.textProperty().addListener((_, _, _) -> filterLoans());
        statusInput.textProperty().addListener((_, _, _) -> filterLoans());
        setUpLoansFieldsTv();
        exportBtn.setOnAction(_ -> exportLoans());
    }

    /**
     * Sets up the export loans fields CheckComboBox.
     */
    private void setUpLoansFieldsTv() {
        exportLoansFieldsTv.getItems().addAll(LoanExportField.values());
        exportLoansFieldsTv.setConverter(new StringConverter<>() {
            @Override
            public String toString(LoanExportField loanExportField) {
                return loanExportField.getDisplayName();
            }

            @Override
            public LoanExportField fromString(String s) {
                return LoanExportField.valueOf(s);
            }
        });
        exportLoansFieldsTv.setTitle("Fields to Export");
        exportLoansFieldsTv.getCheckModel().checkAll();
    }

    /**
     * Exports the loans to a CSV file.
     */
    private void exportLoans() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Export Loans to CSV");
        fileChooser.setInitialFileName("loans.csv");
        File file = fileChooser.showSaveDialog(exportBtn.getScene().getWindow());

        if (file != null) {
            List<LoanExportField> checkModel = exportLoansFieldsTv.getCheckModel().getCheckedItems();

            if (checkModel.isEmpty()) {
                showErrorMessage("Error", "Please select at least one field to export");
                return;
            }
            Result<Void> result = this.loanController.exportLoansToCSV(file, sortedLoansList, checkModel);
            if(result.isSuccess()){
                showSuccessMessage("Success", "Loans exported successfully");
            } else {
                showErrorMessage("Error", result.getErrorMessage());
            }
        }
    }

    /**
     * Sets up the MasterDetailPane for the loans table.
     *
     * @param table the loans table
     */
    private void setUpLoansMasterDetailPane(TableView<Loan> table) {
        this.loansMasterDetailPane.setMasterNode(table);
        this.loansMasterDetailPane.setDetailSide(Side.BOTTOM);
        this.loansMasterDetailPane.setDetailNode(createLoanDetailNode());
        this.loansMasterDetailPane.setDividerPosition(0.7);
        this.loansMasterDetailPane.setShowDetailNode(true);
    }

    /**
     * Creates and returns a node displaying the loan details.
     *
     * @return a GridPane containing the loan details
     */
    private Node createLoanDetailNode() {
        GridPane detailGrid = new GridPane();
        detailGrid.setHgap(25);
        detailGrid.setVgap(5);

        // loan date, due date, deturn date
        Label loanDateLabel = new Label("Loan Date:");
        Text loanDateText = new Text();
        Label dueDateLabel = new Label("Due Date:");
        Text dueDateText = new Text();
        Label returnDateLabel = new Label("Return Date:");
        Text returnDateText = new Text();

        detailGrid.add(loanDateLabel, 0, 0);
        detailGrid.add(loanDateText, 1, 0);
        detailGrid.add(dueDateLabel, 0, 1);
        detailGrid.add(dueDateText, 1, 1);
        detailGrid.add(returnDateLabel, 0, 2);
        detailGrid.add(returnDateText, 1, 2);

        // member, book, price
        Label memberLabel = new Label("Member:");
        Text memberNameText = new Text();
        Label bookLabel = new Label("Book:");
        Text bookTitleText = new Text();
        Label priceLabel = new Label("Price:");
        Text priceText = new Text();

        detailGrid.add(memberLabel, 2, 0);
        detailGrid.add(memberNameText, 3, 0);
        detailGrid.add(bookLabel, 2, 1);
        detailGrid.add(bookTitleText, 3, 1);
        detailGrid.add(priceLabel, 2, 2);
        detailGrid.add(priceText, 3, 2);

        // loan status
        PopOver loanStatusPopOver = new PopOver();
        loanStatusPopOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_CENTER);
        loanStatusPopOver.setDetachable(true);
        loanStatusPopOver.setHeaderAlwaysVisible(true);

        Button loanStatusButton = new Button("Add Return Date");
        loanStatusButton.setOnAction(_ -> {
            Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(selectedLoan)) {
                loanStatusPopOver.setContentNode(createStatusPopOver(selectedLoan, loanStatusPopOver, loansTable));
                loanStatusPopOver.show(loanStatusButton);
            }
        });

        Button deleteLoanButton = new Button("Delete Loan");
        deleteLoanButton.setOnAction(_ -> {
            Loan selectedLoan = loansTable.getSelectionModel().getSelectedItem();
            if (Objects.nonNull(selectedLoan)) {
                Result<UUID> result = this.loanController.deleteLoanById(selectedLoan.getId());
                if(result.isSuccess()){
                    loans.remove(result.getData());
                    showSuccessMessage("Loan Deleted", "Loan has been successfully deleted");
                } else {
                    showErrorMessage("Error", result.getErrorMessage());
                }
            }
        });

        detailGrid.add(loanStatusButton, 0, 4);
        detailGrid.add(deleteLoanButton, 1, 4);

        loansTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, selectedLoan) -> {
            if (Objects.nonNull(selectedLoan)) {
                // member info
                Result<Member> memberResult = memberController.getMemberById(selectedLoan.getMemberId());
                memberNameText.setText(memberResult.isSuccess() ? memberResult.getData().getName() : "not found");

                // book info
                Result<Book> bookResult = bookController.getBookById(selectedLoan.getBookId());
                bookTitleText.setText(bookResult.isSuccess() ? bookResult.getData().getTitle() : "not found");

                // price
                priceText.setText(String.valueOf(selectedLoan.getPrice()));

                // loan dates
                loanDateText.setText(selectedLoan.getLoanDate().toString());
                dueDateText.setText(selectedLoan.getDueDate().toString());
                returnDateText.setText(selectedLoan.getReturnDate() != null ? selectedLoan.getReturnDate().toString() : "-");
                loanStatusButton.setDisable(selectedLoan.getReturnDate() != null);

            } else {
                memberNameText.setText("");
                bookTitleText.setText("");
                priceText.setText("");
                loanDateText.setText("");
                dueDateText.setText("");
                returnDateText.setText("");
            }
        });

        return detailGrid;
    }

    /**
     * Creates and returns a node for changing the loan status.
     *
     * @param selectedLoan the selected loan
     * @param loanStatusPopOver the PopOver for changing the loan status
     * @param loansTable the loans table
     * @return a GridPane containing the loan status change UI
     */
    private Node createStatusPopOver(Loan selectedLoan, PopOver loanStatusPopOver, TableView<Loan> loansTable) {
        loanStatusPopOver.setTitle("Change Loan Status " + selectedLoan.getId().toString().substring(0, 8));

        GridPane statusGrid = new GridPane();
        statusGrid.setHgap(30);
        statusGrid.setVgap(20);
        statusGrid.setPadding(new Insets(20));

        //date
        Label returnDateLabel = new Label("Return Date:");

        DatePicker returnDatePicker = new DatePicker();
        returnDatePicker.setValue(LocalDate.now());
        returnDatePicker.setPromptText("Return Date");
        returnDatePicker.setPrefWidth(200);

        Button okBtn = new Button("Ok");
        okBtn.setOnAction(_ -> {
            if(returnDatePicker.getValue().isAfter(LocalDate.now())){
                showErrorMessage("Invalid Return Date", "Return date cannot be in the future");
                return;
            }

            if(returnDatePicker.getValue().isBefore(selectedLoan.getLoanDate())){
                showErrorMessage("Invalid Return Date", "Return date cannot be before loan date");
                return;
            }

            Result<Loan> result = this.loanController.handleLoanReturn(selectedLoan.getId(), returnDatePicker.getValue());
            if(result.isSuccess()){
                selectedLoan.setReturnDate(returnDatePicker.getValue());
                selectedLoan.setStatus(LoanStatus.RETURNED);
                showSuccessMessage("Loan Updated", "Loan has been successfully updated");
                loansTable.refresh();
            } else {
                showErrorMessage("Error", result.getErrorMessage());
            }
            loanStatusPopOver.hide();
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setOnAction(_ -> loanStatusPopOver.hide());

        okBtn.setPrefWidth(100);
        cancelBtn.setPrefWidth(100);

        statusGrid.add(returnDateLabel, 0, 0);
        statusGrid.add(returnDatePicker, 1, 0);
        statusGrid.add(okBtn, 0, 1);
        GridPane.setHalignment(cancelBtn, HPos.RIGHT);
        statusGrid.add(cancelBtn, 1, 1);

        return statusGrid;
    }

    /**
     * Filters the loans based on the input fields.
     */
    private void filterLoans() {
        filteredLoansList.setPredicate(loan -> {
            String memberEmail = memberInput.getText().toLowerCase(Locale.ROOT);
            Result<Member> memberResult = memberController.getMemberById(loan.getMemberId());
            String memberEmailString = "not found";
            if (memberResult.isSuccess()) {
                memberEmailString = memberResult.getData().getEmail().toLowerCase(Locale.ROOT);
            }

            String bookTitle = bookInput.getText().toLowerCase(Locale.ROOT);
            Result<Book> bookResult = bookController.getBookById(loan.getBookId());
            String bookTitleString = "not found";
            if (bookResult.isSuccess()) {
                bookTitleString = bookResult.getData().getTitle().toLowerCase(Locale.ROOT);
            }

            String status = statusInput.getText().toLowerCase(Locale.ROOT);
            String statusString = loan.getStatus().toString().toLowerCase(Locale.ROOT);

            return memberEmailString.contains(memberEmail)
                    && bookTitleString.contains(bookTitle)
                    && statusString.contains(status);
        });
    }

    /**
     * Sets up the loans table with the specified sorted list.
     *
     * @param sortedLoansList the sorted list of loans
     */
    private void setUpLoansTable(SortedList<Loan> sortedLoansList) {
        // id column
        TableColumn<Loan, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(70);
        idColumn.setComparator(UUID::compareTo);
        idColumn.setSortable(true);

        // member column
        TableColumn<Loan, String> memberColumn = new TableColumn<>("Member");
        memberColumn.setCellValueFactory(param -> {
            UUID memberId = param.getValue().getMemberId();
            Result<Member> memberResult = memberController.getMemberById(memberId);
            if (memberResult.isSuccess()) {
                return new SimpleStringProperty(memberResult.getData().getEmail());
            } else {
                return new SimpleStringProperty("not found");
            }
        });
        memberColumn.setSortable(true);

        // book column
        TableColumn<Loan, String> bookColumn = new TableColumn<>("Book");
        bookColumn.setCellValueFactory(param -> {
            UUID bookId = param.getValue().getBookId();
            Result<Book> bookResult = bookController.getBookById(bookId);
            if (bookResult.isSuccess()) {
                return new SimpleStringProperty(bookResult.getData().getTitle());
            } else {
                return new SimpleStringProperty("not found");
            }
        });
        bookColumn.setSortable(true);


        // loan date column
        TableColumn<Loan, String> loanDate = new TableColumn<>("Loan Date");
        loanDate.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        loanDate.setSortable(true);

        // Status column
        TableColumn<Loan, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        this.loansTable.setSortPolicy(_ -> true);
        this.loansTable.getColumns().addAll(idColumn, memberColumn, bookColumn, loanDate, statusColumn);
        this.loansTable.setItems(sortedLoansList);
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
