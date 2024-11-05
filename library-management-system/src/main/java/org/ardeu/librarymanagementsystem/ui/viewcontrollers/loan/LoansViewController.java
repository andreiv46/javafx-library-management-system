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
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.ardeu.librarymanagementsystem.domain.controllers.BookController;
import org.ardeu.librarymanagementsystem.domain.controllers.LoanController;
import org.ardeu.librarymanagementsystem.domain.controllers.MemberController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.book.Book;
import org.ardeu.librarymanagementsystem.domain.entities.loan.Loan;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.controlsfx.control.MasterDetailPane;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

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
    
    @FXML
    public void initialize() {
        loansTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedLoansList.comparatorProperty().bind(loansTable.comparatorProperty());
        setUpLoansTable(sortedLoansList);
        setUpLoansMasterDetailPane(this.loansTable);
        memberInput.textProperty().addListener((_, _, _) -> filterLoans());
        bookInput.textProperty().addListener((_, _, _) -> filterLoans());
    }

    private void setUpLoansMasterDetailPane(TableView<Loan> table) {
        this.loansMasterDetailPane.setMasterNode(table);
        this.loansMasterDetailPane.setDetailSide(Side.BOTTOM);
        this.loansMasterDetailPane.setDetailNode(createLoanDetailNode());
        this.loansMasterDetailPane.setShowDetailNode(true);
    }

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
                returnDateText.setText(selectedLoan.getReturnDate() != null ? selectedLoan.getReturnDate().toString() : "Not Returned");
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

            return memberEmailString.contains(memberEmail)
                    && bookTitleString.contains(bookTitle);
        });
    }

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

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }
}
