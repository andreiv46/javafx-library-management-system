package org.ardeu.librarymanagementsystem.ui.components;

import javafx.scene.control.Alert;

public class ErrorAlert extends Alert {

    public ErrorAlert(String title) {
        super(AlertType.ERROR);
        this.setTitle(title);
        this.setHeaderText(null);
    }

    public void setContent(String content) {
        this.setContentText(content);
    }

    public void showAlert() {
        this.showAndWait();
    }
}
