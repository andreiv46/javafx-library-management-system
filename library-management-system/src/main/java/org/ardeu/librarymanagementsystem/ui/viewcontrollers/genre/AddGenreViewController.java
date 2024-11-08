package org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.domain.controllers.GenreController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.genre.Genre;
import org.ardeu.librarymanagementsystem.domain.entities.genre.GenreCreationDTO;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

/**
 * AddGenreViewController is responsible for managing the UI and logic for adding a new genre.
 */
public class AddGenreViewController {

    private ScreenViewController screenViewController;
    private final GenreController genreController;

    @FXML
    public MFXTextField nameInput;

    @FXML
    public Button addGenreBtn;

    @FXML
    public Button cancelBtn;

    /**
     * Constructs an AddGenreViewController and initializes the GenreController.
     */
    public AddGenreViewController() {
        genreController = new GenreController();
    }

    /**
     * Adds a new genre using the provided input data.
     */
    private void addGenre() {
        Result<Genre> result = genreController.addGenre(new GenreCreationDTO(nameInput.getText()));
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

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize() {
        addGenreBtn.setOnAction(_ -> addGenre());
        cancelBtn.setOnAction(_ -> {
            nameInput.clear();
            screenViewController.activate(ScreenName.GENRES);
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
}
