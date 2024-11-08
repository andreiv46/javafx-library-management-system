package org.ardeu.librarymanagementsystem.ui.components;

import javafx.scene.control.Alert;

/**
 * ErrorAlert is a custom alert dialog for displaying error messages.
 */
public class ErrorAlert extends Alert {

    /**
     * Constructs an ErrorAlert with the specified title.
     *
     * @param title the title of the error alert
     */
    public ErrorAlert(String title) {
        super(AlertType.ERROR);
        this.setTitle(title);
        this.setHeaderText(null);
    }

    /**
     * Sets the content text of the error alert.
     *
     * @param content the content text to set
     */
    public void setContent(String content) {
        this.setContentText(content);
    }

    /**
     * Displays the error alert and waits for the user to close it.
     */
    public void showAlert() {
        this.showAndWait();
    }
}
