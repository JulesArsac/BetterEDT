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
        connectButton.setDisable(true);
        connectUsername.setStyle("");
        connectPassword.setStyle("");
        connectUsername.setPromptText("");
        connectPassword.setPromptText("");
        try {
            BetterEDT.createUser(connectUsername.getText(), connectPassword.getText());
            BetterEDT.goToMainScreen();
        } catch (UserNotFountException e) {
            connectUsername.setStyle("-fx-border-color: red;");
            connectUsername.setPromptText("Invalid username");
            connectButton.setDisable(false);
        } catch (WrongPasswordException e) {
            connectPassword.setStyle("-fx-border-color: red;");
            connectPassword.setPromptText("Wrong password");
            connectButton.setDisable(false);
        }
    }

    public void create() {
        System.out.println("Creating user with username: " + createUsername.getText() + " and password: " + createPassword.getText());
        createButton.setDisable(true);
    }

}
