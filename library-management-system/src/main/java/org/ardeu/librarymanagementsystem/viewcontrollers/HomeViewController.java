package org.ardeu.librarymanagementsystem.viewcontrollers;

import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.ardeu.librarymanagementsystem.entities.author.Author;
import org.ardeu.librarymanagementsystem.services.AuthorService;
import org.ardeu.librarymanagementsystem.services.registry.ServiceRegistry;

import java.util.UUID;

public class HomeViewController {

    private ScreenController screenController;
    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);

    private ObservableList<String> authorNames;

    @FXML
    public Button screen2Btn;

    @FXML
    public ListView<String> authorLV;

    @FXML
    public Button addAuthorBtn;

    @FXML
    private Label welcomeText;

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

    @FXML
    public void initialize() {

        this.authorNames = FXCollections.observableArrayList(
                authorService.getItems().values().stream()
                        .map(Author::getName)
                        .toList()
        );

        authorLV.setItems(this.authorNames);

        authorService.getItems().addListener((MapChangeListener<UUID, Author>) change -> {
            if(change.wasAdded()){
                authorNames.add(change.getValueAdded().getName());
            }
            if (change.wasRemoved()) {
                authorNames.remove(change.getValueRemoved().getName());
            }
        });
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void onAddAuthorClick() {
        Author author = new Author(UUID.randomUUID(), "Alex", null);
        this.authorService.addAuthor(author);
    }

    public void onClickScreen2Btn() {
        screenController.activate("screen1");
    }
}