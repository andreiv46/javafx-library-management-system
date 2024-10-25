package org.ardeu.librarymanagementsystem;

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

public class HelloController {

    private final AuthorService authorService = ServiceRegistry.getInstance().getService(AuthorService.class);
    private ObservableList<String> authorNames;

    @FXML
    public ListView<String> authorLV;

    @FXML
    public Button addAuthorBtn;

    @FXML
    private Label welcomeText;

    @FXML
    public void initialize() {
        // Load authors from file
//            authorService.load();

        // Convert the authors map values to an ObservableList

        this.authorNames = FXCollections.observableArrayList(
                authorService.getItems().values().stream()
                        .map(Author::getName) // Assuming Author has a getName() method
                        .toList()
        );

        // Set the list of author names to the ListView
        authorLV.setItems(this.authorNames);

        // Listen for changes in the map to update the ListView
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
}