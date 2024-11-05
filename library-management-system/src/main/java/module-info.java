module org.ardeu.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.xml;
    requires MaterialFX;
    requires org.controlsfx.controls;


    opens org.ardeu.librarymanagementsystem to javafx.fxml;
    exports org.ardeu.librarymanagementsystem;
    exports org.ardeu.librarymanagementsystem.domain.entities.book;
    opens org.ardeu.librarymanagementsystem.domain.entities.base to javafx.base;
    exports org.ardeu.librarymanagementsystem.ui.viewcontrollers.book;
    opens org.ardeu.librarymanagementsystem.ui.viewcontrollers.book to javafx.fxml;
    exports org.ardeu.librarymanagementsystem.ui.viewcontrollers.home;
    opens org.ardeu.librarymanagementsystem.ui.viewcontrollers.home to javafx.fxml;
    exports org.ardeu.librarymanagementsystem.ui.viewcontrollers.base;
    opens org.ardeu.librarymanagementsystem.ui.viewcontrollers.base to javafx.fxml;
    exports org.ardeu.librarymanagementsystem.ui.viewcontrollers.member;
    opens org.ardeu.librarymanagementsystem.ui.viewcontrollers.member to javafx.fxml;
    exports org.ardeu.librarymanagementsystem.domain.entities.member;
    opens org.ardeu.librarymanagementsystem.domain.entities.member to javafx.base;
    exports org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre;
    opens org.ardeu.librarymanagementsystem.ui.viewcontrollers.genre to javafx.fxml;
    exports org.ardeu.librarymanagementsystem.domain.entities.genre;
    opens org.ardeu.librarymanagementsystem.domain.entities.genre to javafx.base;
    exports org.ardeu.librarymanagementsystem.ui.viewcontrollers.author;
    opens org.ardeu.librarymanagementsystem.ui.viewcontrollers.author to javafx.fxml;
    exports org.ardeu.librarymanagementsystem.domain.entities.author;
    opens org.ardeu.librarymanagementsystem.domain.entities.author to javafx.base;
    exports org.ardeu.librarymanagementsystem.ui.viewcontrollers.loan;
    opens org.ardeu.librarymanagementsystem.ui.viewcontrollers.loan to javafx.fxml;
    exports org.ardeu.librarymanagementsystem.domain.entities.loan;
    opens org.ardeu.librarymanagementsystem.domain.entities.loan to javafx.base;
    exports org.ardeu.librarymanagementsystem.domain.validators.loan;
    opens org.ardeu.librarymanagementsystem.domain.validators.loan to javafx.base;
}