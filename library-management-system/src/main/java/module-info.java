module org.ardeu.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.ardeu.librarymanagementsystem to javafx.fxml;
    exports org.ardeu.librarymanagementsystem;
}