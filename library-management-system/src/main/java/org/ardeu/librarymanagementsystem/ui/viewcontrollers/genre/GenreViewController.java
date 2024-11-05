package org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre;

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
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

import java.util.UUID;

public class GenreViewController {

    private final GenreController genreController;
    private ScreenViewController screenViewController;
    private final ObservableMap<UUID, Genre> genres;
    private final ObservableList<Genre> genresList;
    private final FilteredList<Genre> filteredGenresList;
    private final SortedList<Genre> sortedGenresList;

    @FXML
    public MFXTextField genreInput;

    @FXML
    public TableView<Genre> genresTable;

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
        });
    }

    @FXML
    public void initialize() {
        genresTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        sortedGenresList.comparatorProperty().bind(genresTable.comparatorProperty());
        setUpGenresTable(sortedGenresList);
        genreInput.textProperty().addListener((_, _, _) -> filterGenres());
    }

    private void filterGenres() {
        filteredGenresList.setPredicate(genre -> {
            String genreName = genreInput.getText().toLowerCase();
            return genre.getName().toLowerCase().contains(genreName);
        });
    }

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

        genresTable.getColumns().addAll(idColumn, nameColumn, numberOfBooksColumn);
        genresTable.setItems(sortedGenresList);
    }

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
    }
}
