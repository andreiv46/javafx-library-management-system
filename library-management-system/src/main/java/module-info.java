module org.ardeu.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens org.ardeu.librarymanagementsystem to javafx.fxml;
    exports org.ardeu.librarymanagementsystem;
    exports org.ardeu.librarymanagementsystem.viewcontrollers;
    opens org.ardeu.librarymanagementsystem.viewcontrollers to javafx.fxml;
}