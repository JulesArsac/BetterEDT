module org.example.betteredt {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.betteredt to javafx.fxml;
    exports org.example.betteredt;
}