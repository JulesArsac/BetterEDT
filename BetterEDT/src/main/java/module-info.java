module org.example.betteredt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mnode.ical4j.core;
    requires java.desktop;


    opens org.example.betteredt to javafx.fxml;
    exports org.example.betteredt;
}