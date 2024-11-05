package org.ardeu.librarymanagementsystem.ui.components;

import javafx.scene.control.Alert;

public class SuccessAlert extends Alert {
    public SuccessAlert(String title) {
        super(AlertType.INFORMATION);
        this.setTitle(title);
        this.setHeaderText(null);
    }

    public void setContent(String content){
        this.setContentText(content);
    }

    public void showAlert(){
        this.showAndWait();
    }
}
