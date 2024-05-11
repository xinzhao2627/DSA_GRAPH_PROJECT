module com.example.dsa {
    requires javafx.controls;
    requires javafx.fxml;
    opens com.example.dsa to javafx.fxml;
    exports com.example.dsa;
}