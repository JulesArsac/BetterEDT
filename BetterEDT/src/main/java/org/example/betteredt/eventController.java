package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class eventController {

    @FXML
    VBox root;
    @FXML
    Label time;
    @FXML
    Label room;
    @FXML
    Label title;
    @FXML
    Label prof;
    @FXML
    Label type;

    public void setTime(String time) {
        this.time.setText(time);
    }

    public void setRoom(String room) {
        this.room.setText(room);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setProf(String prof) {
        this.prof.setText(prof);
    }

    public void setType(String type) {
        this.type.setText(type);
    }
}
