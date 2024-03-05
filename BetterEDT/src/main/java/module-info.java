module org.example.betteredt {
    requires javafx.controls;
    requires javafx.fxml;
    requires ical4j;


    opens org.example.betteredt to javafx.fxml;
    exports org.example.betteredt;
}