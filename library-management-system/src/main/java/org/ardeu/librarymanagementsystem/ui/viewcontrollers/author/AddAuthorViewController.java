package org.ardeu.librarymanagementsystem.ui.viewcontrollers.author;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorCreationDTO;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

/**
 * AddAuthorViewController is responsible for managing the UI and logic for adding a new author.
 */
public class AddAuthorViewController {

    private ScreenViewController screenViewController;
    private final AuthorController authorController;

    @FXML
    public MFXTextField nameInput;

    @FXML
    public Button addAuthorBtn;

    @FXML
    public Button cancelBtn;

    /**
     * Constructs an AddAuthorViewController and initializes the AuthorController.
     */
    public AddAuthorViewController() {
        authorController = new AuthorController();
    }

    /**
     * Initializes the controller and sets up the UI components.
     */
    @FXML
    public void initialize(){
        addAuthorBtn.setOnAction(_ -> addAuthor());
        cancelBtn.setOnAction(_ -> onCancel());
    }

    /**
     * Handles the cancel action, clearing input fields and navigating back to the authors screen.
     */
    private void onCancel() {
        nameInput.clear();
        screenViewController.activate(ScreenName.AUTHORS);
    }

    /**
     * Adds a new author using the provided input data.
     */
    private void addAuthor() {
        Result<Author> result = authorController.addAuthor(new AuthorCreationDTO(nameInput.getText()));
        if(result.isSuccess()){
            showSuccessMessage(
                    "Author added", "Author with name: " +
                    result.getData().getName() + " added successfully");
            nameInput.clear();
            screenViewController.activate(ScreenName.AUTHORS);
        } else {
            showErrorMessage("Couldn't add author", result.getErrorMessage());
        }
    }

    /**
     * Sets the screen view controller for this view controller.
     * @param screenViewController the screen view controller to set
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
