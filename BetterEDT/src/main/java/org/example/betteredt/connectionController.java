package org.example.betteredt;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class connectionController implements Initializable {


    @FXML
    AnchorPane rootPane;
    @FXML
    private HBox hBox;
    @FXML
    private VBox connectBox;
    @FXML
    private TextField connectUsername;
    @FXML
    private TextField connectPassword;
    @FXML
    private Button connectButton;


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

        rootPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                connect();
            }
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
            BetterEDT.goToFormationScreen();
        } catch (UserNotFountException e) {
            connectUsername.setStyle("-fx-border-color: red;");
            connectUsername.setPromptText("Pseudo invalide");
            connectButton.setDisable(false);
        } catch (WrongPasswordException e) {
            connectPassword.setStyle("-fx-border-color: red;");
            connectPassword.setPromptText("Mot de passe invalide");
            connectButton.setDisable(false);
        }
    }
}
