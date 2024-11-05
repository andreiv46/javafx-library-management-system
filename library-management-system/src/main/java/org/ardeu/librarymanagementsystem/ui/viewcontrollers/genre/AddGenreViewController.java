package org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreDTO;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

public class AddGenreViewController {

    private ScreenViewController screenViewController;
    private final GenreController genreController;

    @FXML
    public MFXTextField nameInput;

    @FXML
    public Button addGenreBtn;

    @FXML
    public Button cancelBtn;

    public AddGenreViewController() {
        genreController = new GenreController();
    }

    private void addGenre() {
        Result<Genre> result = genreController.addGenre(new GenreDTO(nameInput.getText()));
        if(result.isSuccess()){
            showSuccessMessage(
                    "Genre added", "Genre with name: " +
                    result.getData().getName() + " added successfully");

            nameInput.clear();
            screenViewController.activate(ScreenName.GENRES);
        } else {
            showErrorMessage("Couldn't add genre", result.getErrorMessage());
        }
    }

    @FXML
    public void initialize() {
        addGenreBtn.setOnAction(_ -> addGenre());
        cancelBtn.setOnAction(_ -> {
            nameInput.clear();
            screenViewController.activate(ScreenName.GENRES);
        });
    }

    public void setScreenViewController(ScreenViewController screenViewController) {
        this.screenViewController = screenViewController;
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
