package org.example.betteredt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class mainController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private ComboBox periodChoice;
    @FXML
    private ToggleButton darkSasuke;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        periodChoice.getItems().addAll("Month" , "Week" , "Day");
        periodChoice.getSelectionModel().selectFirst();
        periodChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Selected period: " + newVal);
        });

        darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");

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
}