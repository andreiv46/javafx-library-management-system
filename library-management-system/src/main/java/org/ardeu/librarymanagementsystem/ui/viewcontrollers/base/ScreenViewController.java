package org.ardeu.librarymanagementsystem.ui.viewcontrollers.base;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;


public class ScreenViewController {

    private final BorderPane rootPane;
    private final Map<ScreenName, Pane> screenMap = new HashMap<>();
    private final Map<ScreenName, Object> controllerMap = new HashMap<>();

    public ScreenViewController(BorderPane rootPane) {
        this.rootPane = rootPane;
    }

    public void addScreen(ScreenName name, Pane pane, Object controller) {
        screenMap.put(name, pane);
        controllerMap.put(name, controller);
    }

    public void activate(ScreenName name) {
        Pane pane = screenMap.get(name);
        if (pane != null) {
            rootPane.setCenter(pane);
        }
    }

    public Object getController(ScreenName name) {
        return controllerMap.get(name);
    }
}
