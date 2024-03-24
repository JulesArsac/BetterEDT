package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import static org.example.betteredt.BetterEDT.getConn;
import static org.example.betteredt.BetterEDT.getUser;

public class createEmailController implements Initializable {


    public AnchorPane rootPane;
    @FXML
    private Button sendButton;
    @FXML
    private ToggleButton darkSasuke;
    @FXML
    private GridPane mainGrid;

    @FXML
    void openNewScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("newScene.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onDarkSasukeClick() {
        if (darkSasuke.isSelected()) {
            BetterEDT.goDarkMode();
            darkSasuke.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: #FFFFFF; -fx-border-color: #222222");


        } else {
            BetterEDT.goLightMode();
            darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainGrid.prefHeightProperty().bind(Bindings.subtract(rootPane.heightProperty(), 100));
    }

    public void switchToPersonalSchedule(ActionEvent actionEvent) {
        BetterEDT.goToPersonalScreen();
    }

    public void switchToMainScreen(ActionEvent actionEvent) {
        BetterEDT.goToMainScreen();
    }

    public void switchToSalleSchedule(ActionEvent actionEvent) {
        BetterEDT.switchToSalleSchedule();
    }

}
