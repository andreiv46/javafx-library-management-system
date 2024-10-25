module org.ardeu.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens org.ardeu.librarymanagementsystem to javafx.fxml;
    exports org.ardeu.librarymanagementsystem;
}