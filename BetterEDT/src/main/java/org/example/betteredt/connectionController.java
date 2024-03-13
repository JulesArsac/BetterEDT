package org.example.betteredt;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class connectionController implements Initializable {

    Timer connectTimer = new Timer();
    Timer createTimer = new Timer();

    @FXML
    AnchorPane rootPane;
    @FXML
    private HBox hBox;
    @FXML
    private VBox connectBox;
    @FXML
    private VBox createBox;
    @FXML
    private TextField connectUsername;
    @FXML
    private TextField connectPassword;
    @FXML
    private TextField createUsername;
    @FXML
    private TextField createPassword;
    @FXML
    private Button connectButton;
    @FXML
    private Button createButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            hBox.setPrefWidth(newVal.doubleValue());
            hBox.setMaxWidth(newVal.doubleValue());
        });
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            hBox.setPrefHeight(newVal.doubleValue());
            hBox.setMaxHeight(newVal.doubleValue());
        });
    }

    public void connect() {
        System.out.println("Connecting with username: " + connectUsername.getText() + " and password: " + connectPassword.getText());
        connectTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                connectButton.setDisable(false);
            }
        }, 1000);
        connectButton.setDisable(true);
    }

    public void create() {
        System.out.println("Creating user with username: " + createUsername.getText() + " and password: " + createPassword.getText());
        createTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                createButton.setDisable(false);
            }
        }, 1000);
        createButton.setDisable(true);
    }

}
