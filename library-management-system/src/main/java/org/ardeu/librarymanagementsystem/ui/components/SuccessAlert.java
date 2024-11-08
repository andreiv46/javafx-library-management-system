package org.ardeu.librarymanagementsystem.ui.components;

import javafx.scene.control.Alert;

/**
 * SuccessAlert is a custom alert dialog for displaying success messages.
 */
public class SuccessAlert extends Alert {

    /**
     * Constructs a SuccessAlert with the specified title.
     *
     * @param title the title of the success alert
     */
    public SuccessAlert(String title) {
        super(AlertType.INFORMATION);
        this.setTitle(title);
        this.setHeaderText(null);
    }

    /**
     * Sets the content text of the success alert.
     *
     * @param content the content text to set
     */
    public void setContent(String content){
        this.setContentText(content);
    }

    /**
     * Displays the success alert and waits for the user to close it.
     */
    public void showAlert(){
        this.showAndWait();
    }
}
