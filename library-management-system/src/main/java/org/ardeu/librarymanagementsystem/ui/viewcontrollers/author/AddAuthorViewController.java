package org.ardeu.librarymanagementsystem.ui.viewcontrollers.author;

import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.domain.controllers.AuthorController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.author.Author;
import org.ardeu.librarymanagementsystem.domain.entities.author.AuthorDTO;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

public class AddAuthorViewController {


    private ScreenViewController screenViewController;
    private final AuthorController authorController;

    @FXML
    public MFXTextField nameInput;

    @FXML
    public Button addAuthorBtn;

    @FXML
    public Button cancelBtn;

    public AddAuthorViewController() {
        authorController = new AuthorController();
    }

    @FXML
    public void initialize(){
        addAuthorBtn.setOnAction(_ -> addAuthor());
        cancelBtn.setOnAction(_ -> onCancel());
    }

    private void onCancel() {
        nameInput.clear();
        screenViewController.activate(ScreenName.AUTHORS);
    }

    private void addAuthor() {
        Result<Author> result = authorController.addAuthor(new AuthorDTO(nameInput.getText()));
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
