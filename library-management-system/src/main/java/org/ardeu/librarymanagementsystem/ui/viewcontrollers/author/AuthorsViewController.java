package org.ardeu.librarymanagementsystem.ui.viewcontrollers.author;

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
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

import java.util.UUID;

public class AuthorsViewController {

    private ScreenViewController screenViewController;
    private final AuthorController authorController;
    private final ObservableMap<UUID, Author> authors;
    private final ObservableList<Author> authorsList;
    private final FilteredList<Author> filteredAuthorsList;
    private final SortedList<Author> sortedAuthorsList;

    @FXML
    public MFXTextField authorInput;

    @FXML
    public TableView<Author> authorsTable;

    @FXML
    public void initialize(){
        authorsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedAuthorsList.comparatorProperty().bind(authorsTable.comparatorProperty());
        setUpAuthorsTable(sortedAuthorsList);
        authorInput.textProperty().addListener((_, _, _) -> filterAuthors());
    }

    private void filterAuthors() {
        filteredAuthorsList.setPredicate(author -> {
            String authorName = authorInput.getText().toLowerCase();
            return author.getName().toLowerCase().contains(authorName);
        });
    }

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

        authorsTable.getColumns().addAll(idColumn, nameColumn, numberOfBooksColumn);
        authorsTable.setItems(sortedAuthorsList);
    }

    public AuthorsViewController() {
        authorController = new AuthorController();

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
        });
    }

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }
}
