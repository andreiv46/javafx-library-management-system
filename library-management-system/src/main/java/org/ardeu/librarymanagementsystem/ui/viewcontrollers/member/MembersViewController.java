package org.ardeu.librarymanagementsystem.ui.viewcontrollers.member;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleIntegerProperty;
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
import org.ardeu.librarymanagementsystem.domain.controllers.MemberController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.entities.member.MemberExportField;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;
import org.controlsfx.control.CheckComboBox;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * MembersViewController is responsible for managing the UI and logic for displaying and exporting members.
 */
public class MembersViewController {

    private final MemberController memberController;
    private ScreenViewController screenViewController;
    private final ObservableMap<UUID, Member> members;
    private final ObservableList<Member> membersList;
    private final FilteredList<Member> filteredMembersList;
    private final SortedList<Member> sortedMembersList;

    @FXML
    public MFXTextField nameInput;

    @FXML
    public TableView<Member> membersTable;

    @FXML
    public MFXTextField emailInput;

    @FXML
    public CheckComboBox<MemberExportField> exportMemberFieldsTv;

    @FXML
    public Button exportBtn;

    /**
     * Initializes the MembersViewController.
     */
    @FXML
    public void initialize() {
        membersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        sortedMembersList.comparatorProperty().bind(membersTable.comparatorProperty());
        setUpMembersTable(sortedMembersList);

        nameInput.textProperty().addListener((_, _, _) -> filterMembers());
        emailInput.textProperty().addListener((_, _, _) -> filterMembers());

        setUpMembersFieldsTv();
        exportBtn.setOnAction(_ -> exportMembers());
    }

    /**
     * Sets up the export member fields CheckComboBox.
     */
    private void setUpMembersFieldsTv() {
        exportMemberFieldsTv.getItems().addAll(MemberExportField.values());
        exportMemberFieldsTv.setConverter(new StringConverter<>() {
            @Override
            public String toString(MemberExportField memberExportField) {
                return memberExportField.getDisplayName();
            }

            @Override
            public MemberExportField fromString(String s) {
                return MemberExportField.valueOf(s);
            }
        });
        exportMemberFieldsTv.setTitle("Fields to export");
        exportMemberFieldsTv.getCheckModel().checkAll();
    }

    /**
     * Exports the members to a CSV file.
     */
    private void exportMembers() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setTitle("Export Members to CSV");
        fileChooser.setInitialFileName("members.csv");
        File file = fileChooser.showSaveDialog(exportBtn.getScene().getWindow());
        if (file != null) {
            List<MemberExportField> checkModel = exportMemberFieldsTv.getCheckModel().getCheckedItems();

            if (checkModel.isEmpty()) {
                showErrorMessage("Error", "Please select at least one field to export");
                return;
            }

            Result<Void> result = this.memberController.exportMembersToCSV(file, sortedMembersList, checkModel);
            if(result.isSuccess()){
                showSuccessMessage("Success", "Members exported successfully");
            } else {
                showErrorMessage("Error", result.getErrorMessage());
            }
        }
    }

    /**
     * Filters the members based on the input fields.
     */
    private void filterMembers() {
        filteredMembersList.setPredicate(member -> {
            String name = nameInput.getText().toLowerCase(Locale.ROOT);
            String email = emailInput.getText().toLowerCase(Locale.ROOT);
            return member.getName().toLowerCase(Locale.ROOT).contains(name) &&
                    member.getEmail().toLowerCase(Locale.ROOT).contains(email);
        });
    }

    /**
     * Sets up the members table with the specified sorted list.
     *
     * @param sortedMembersList the sorted list of members
     */
    private void setUpMembersTable(ObservableList<Member> sortedMembersList) {
        // id column
        TableColumn<Member, UUID> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(70);
        idColumn.setComparator(UUID::compareTo);
        idColumn.setSortable(true);

        // name column
        TableColumn<Member, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setComparator(String::compareTo);
        nameColumn.setSortable(true);

        // email column
        TableColumn<Member, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setComparator(String::compareTo);
        emailColumn.setSortable(true);

        // number of books lent column
        TableColumn<Member, Integer> numberOfBooksLentColumn = new TableColumn<>("Nr. of Books Lent");
        numberOfBooksLentColumn.setCellValueFactory(param -> {
            Member member = param.getValue();
            return new SimpleIntegerProperty(member.getLoans().size()).asObject();
        });
        numberOfBooksLentColumn.setPrefWidth(50);
        numberOfBooksLentColumn.setComparator(Integer::compareTo);
        numberOfBooksLentColumn.setSortable(true);

        membersTable.setSortPolicy(_ -> true);
        membersTable.getColumns().addAll(idColumn, nameColumn, emailColumn, numberOfBooksLentColumn);
        membersTable.setItems(sortedMembersList);
    }

    /**
     * Constructs a MembersViewController and initializes the member data.
     */
    public MembersViewController() {
        memberController = new MemberController();

        members = memberController.getAllMembers().getData();
        membersList = FXCollections.observableArrayList(members.values().stream().toList());
        filteredMembersList = new FilteredList<>(membersList, _ -> true);
        sortedMembersList = new SortedList<>(filteredMembersList);

        members.addListener((MapChangeListener<UUID, Member>) _ -> {
            membersList.setAll(members.values());
            membersTable.refresh();
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
     * Updates the members table.
     */
    public void updateTable() {
        membersTable.refresh();
    }
}