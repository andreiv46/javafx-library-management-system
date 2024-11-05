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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ardeu.librarymanagementsystem.domain.controllers.MemberController;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

import java.util.Locale;
import java.util.UUID;

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
    public void initialize() {
        membersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        sortedMembersList.comparatorProperty().bind(membersTable.comparatorProperty());
        setUpMembersTable(sortedMembersList);

        nameInput.textProperty().addListener((_, _, _) -> filterMembers());
        emailInput.textProperty().addListener((_, _, _) -> filterMembers());
    }

    private void filterMembers() {
        filteredMembersList.setPredicate(member -> {
            String name = nameInput.getText().toLowerCase(Locale.ROOT);
            String email = emailInput.getText().toLowerCase(Locale.ROOT);
            return member.getName().toLowerCase(Locale.ROOT).contains(name) &&
                    member.getEmail().toLowerCase(Locale.ROOT).contains(email);
        });
    }

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

        //number of books lent column
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

    public MembersViewController() {
        memberController = new MemberController();

        members = memberController.getAllMembers().getData();
        membersList = FXCollections.observableArrayList(members.values().stream().toList());
        filteredMembersList = new FilteredList<>(membersList, _ -> true);
        sortedMembersList = new SortedList<>(filteredMembersList);

        members.addListener((MapChangeListener<UUID,Member>) change -> {
            if (change.wasAdded()) {
                membersList.add(change.getValueAdded());
            } else if (change.wasRemoved()) {
                membersList.remove(change.getValueRemoved());
            }
        });
    }

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }
}
