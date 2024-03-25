package org.example.betteredt;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class eventController implements Initializable {

    private EventCalendrier event = null;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        BooleanBinding firstBinding = root.heightProperty().greaterThan(20);
        BooleanBinding secondBinding = root.heightProperty().greaterThan(40);
        BooleanBinding thirdBinding = root.heightProperty().greaterThan(60);
        BooleanBinding fourthBinding = root.heightProperty().greaterThan(82);
        BooleanBinding fifthBinding = root.heightProperty().greaterThan(105);

        time.visibleProperty().bind(firstBinding);
        room.visibleProperty().bind(secondBinding);
        title.visibleProperty().bind(thirdBinding);
        prof.visibleProperty().bind(fourthBinding);
        type.visibleProperty().bind(fifthBinding);

        //Check if the type is "Evaluation" and change the color of the event
        type.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Evaluation")) {
                if (event != null) {
                    if (event.getColor() != null) {
                        return;
                    }
                }
                root.setStyle("-fx-background-color: #f56e64; -fx-border-color: black; -fx-border-width: 1px;");
            }
        });

        time.setFont(Font.font("System", FontWeight.BOLD, 12));
    }

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

    public void setEvent(EventCalendrier event) {
        this.event = event;
        if (event.getColor() != null) {
            root.setStyle("-fx-background-color: #" + event.getColor() + "; -fx-border-color: black; -fx-border-width: 1px;");
        }
    }


    public void sendAnEmail(MouseEvent mouseEvent) {
        String lowercaseInput = prof.getText().toLowerCase();
        String[] words = lowercaseInput.split(" ");
        String output = String.join(".", words);

        String mailto = "mailto:"+output+"@univ-avignon.fr";
        try {
            // Encode the mailto URI to handle special characters (e.g., spaces)
            URI uri = new URI(mailto.replace(" ", "%20"));

            // Use the Desktop class to open the default mail client with the pre-populated email
            Desktop.getDesktop().browse(uri);
        } catch (URISyntaxException | IOException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }
}
