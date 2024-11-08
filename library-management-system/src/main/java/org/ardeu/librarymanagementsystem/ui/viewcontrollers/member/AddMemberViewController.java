package org.ardeu.librarymanagementsystem.ui.viewcontrollers.member;

import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.ardeu.librarymanagementsystem.domain.controllers.MemberController;
import org.ardeu.librarymanagementsystem.domain.controllers.result.Result;
import org.ardeu.librarymanagementsystem.domain.entities.member.Member;
import org.ardeu.librarymanagementsystem.domain.entities.member.MemberCreationDTO;
import org.ardeu.librarymanagementsystem.domain.validators.member.MemberDTOValidator;
import org.ardeu.librarymanagementsystem.ui.components.ErrorAlert;
import org.ardeu.librarymanagementsystem.ui.components.SuccessAlert;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenName;
import org.ardeu.librarymanagementsystem.ui.viewcontrollers.base.ScreenViewController;

import java.util.List;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

/**
 * AddMemberViewController is responsible for managing the UI and logic for adding a new member.
 */
public class AddMemberViewController {

    private ScreenViewController screenViewController;
    private final MemberController memberController;

    @FXML
    public MFXTextField nameInput;

    @FXML
    public Button addGenreBtn;

    @FXML
    public Button cancelBtn;

    @FXML
    public MFXTextField emailInput;

    @FXML
    public Label nameValidationLabel;

    @FXML
    public Label emailValidationLabel;

    /**
     * Initializes the AddMemberViewController.
     */
    @FXML
    public void initialize() {
        initializeNameInputValidation();
        initializeEmailInputValidation();

        addGenreBtn.setOnAction(_ -> addMember());
        cancelBtn.setOnAction(_ -> onCancel());
    }

    /**
     * Handles the cancel action, clearing input fields and navigating back to the members screen.
     */
    private void onCancel() {
        nameInput.clear();
        emailInput.clear();
        nameValidationLabel.setVisible(false);
        emailValidationLabel.setVisible(false);
        screenViewController.activate(ScreenName.MEMBERS);
    }

    /**
     * Initializes validation for the name input field.
     */
    private void initializeNameInputValidation() {
        Constraint lengthConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Name must be at least 2 characters long")
                .setCondition(nameInput.textProperty().length().greaterThanOrEqualTo(2))
                .get();

        nameInput.getValidator().constraint(lengthConstraint);

        nameInput.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                nameValidationLabel.setVisible(false);
                nameInput.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        nameInput.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = nameInput.validate();
                if (!constraints.isEmpty()) {
                    nameInput.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    nameValidationLabel.setText(constraints.get(0).getMessage());
                    nameValidationLabel.setVisible(true);
                }
            }
        });
    }

    /**
     * Initializes validation for the email input field.
     */
    private void initializeEmailInputValidation() {
        Constraint emailConstraint = Constraint.Builder.build()
                .setSeverity(Severity.ERROR)
                .setMessage("Email is not valid.\nEmail must be in the format: john@example.com")
                .setCondition(Bindings.createBooleanBinding(() ->
                        emailInput.getText().matches(MemberDTOValidator.EMAIL_REGEX), emailInput.textProperty()
                ))
                .get();

        emailInput.getValidator().constraint(emailConstraint);

        emailInput.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                emailValidationLabel.setVisible(false);
                emailInput.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
            }
        });

        emailInput.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue) {
                List<Constraint> constraints = emailInput.validate();
                if (!constraints.isEmpty()) {
                    emailInput.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
                    emailValidationLabel.setText(constraints.get(0).getMessage());
                    emailValidationLabel.setVisible(true);
                }
            }
        });
    }

    /**
     * Adds a new member using the provided input data.
     */
    private void addMember() {
        Result<Member> result = memberController.addMember(new MemberCreationDTO(nameInput.getText(), emailInput.getText()));
        if (result.isSuccess()) {
            showSuccessMessage(
                    "Member added", "Member with name: " +
                            result.getData().getName() + " added successfully");
            nameInput.clear();
            emailInput.clear();
            screenViewController.activate(ScreenName.MEMBERS);
        } else {
            showErrorMessage("Couldn't add member", result.getErrorMessage());
        }
    }

    /**
     * Constructs an AddMemberViewController and initializes the MemberController.
     */
    public AddMemberViewController() {
        memberController = new MemberController();
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