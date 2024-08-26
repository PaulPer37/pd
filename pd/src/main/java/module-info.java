module ec.edu.espol.pd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    opens ec.edu.espol.pd to javafx.fxml;
    exports ec.edu.espol.pd;
}
