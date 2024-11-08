package org.ardeu.librarymanagementsystem.ui.viewcontrollers.base;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;


/**
 * ScreenViewController is responsible for managing and switching between different screens in the application.
 */
public class ScreenViewController {

    private final BorderPane rootPane;
    private final Map<ScreenName, Pane> screenMap = new HashMap<>();
    private final Map<ScreenName, Object> controllerMap = new HashMap<>();

    /**
     * Constructs a ScreenViewController with the specified root pane.
     *
     * @param rootPane the root BorderPane of the application
     */
    public ScreenViewController(BorderPane rootPane) {
        this.rootPane = rootPane;
    }

    /**
     * Adds a screen to the controller.
     *
     * @param name the name of the screen
     * @param pane the Pane representing the screen
     * @param controller the controller associated with the screen
     */
    public void addScreen(ScreenName name, Pane pane, Object controller) {
        screenMap.put(name, pane);
        controllerMap.put(name, controller);
    }

    /**
     * Activates the specified screen, making it visible in the root pane.
     *
     * @param name the name of the screen to activate
     */
    public void activate(ScreenName name) {
        Pane pane = screenMap.get(name);
        if (pane != null) {
            rootPane.setCenter(pane);
        }
    }

    /**
     * Retrieves the controller associated with the specified screen.
     *
     * @param name the name of the screen
     * @return the controller associated with the screen, or null if not found
     */
    public Object getController(ScreenName name) {
        return controllerMap.get(name);
    }
}
