package org.ardeu.librarymanagementsystem.viewcontrollers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class Screen1Controller {

    private ScreenController screenController;

    @FXML
    public Button goToHomeBtn;

    @FXML
    public void initialize(){
        System.out.println("Screen two");
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

    public void gotToHomeBtnOnClick() {
        screenController.activate("home");
    }
}
