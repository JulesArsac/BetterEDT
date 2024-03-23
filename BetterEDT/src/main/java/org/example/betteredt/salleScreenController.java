package org.example.betteredt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class salleScreenController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private ComboBox periodChoice;
    @FXML
    private ToggleButton darkSasuke;
    @FXML
    GridPane edtGrid;
    @FXML
    AnchorPane rootPane;
    @FXML
    GridPane filterPane;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        periodChoice.getItems().addAll("Month" , "Week" , "Day");
        periodChoice.getSelectionModel().selectFirst();
        periodChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Selected period: " + newVal);
        });

        darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");

        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            edtGrid.setPrefWidth(newVal.doubleValue()-160);
            edtGrid.setMaxWidth(newVal.doubleValue()-160);

            if (newVal.doubleValue() < 160) {
                filterPane.setPrefWidth(newVal.doubleValue());
                filterPane.setMaxWidth(newVal.doubleValue());
            }
            else {
                filterPane.setPrefWidth(160);
                filterPane.setMaxWidth(160);
            }
        });
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            edtGrid.setPrefHeight(newVal.doubleValue()-129);
            edtGrid.setMaxHeight(newVal.doubleValue()-129);

            filterPane.setPrefHeight(newVal.doubleValue()-150);
            filterPane.setMaxHeight(newVal.doubleValue()-150);

        });

        for (int i= 0; i < 7; i++) {
            Label label = new Label(i + "");
            edtGrid.add(label, i, 0);
        }

        List<EventCalendrier> mainList = Parser.startParser();
        if (mainList == null) {
            System.out.println("Error while parsing the file");
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

    public void switchToPersonalSchedule(ActionEvent actionEvent) {
        //do some visual change here
        BetterEDT.goToPersonalScreen();
    }

    public void switchToMainScreen(ActionEvent actionEvent) {
        BetterEDT.goToMainScreen();
    }

    public void switchToSalleSchedule(ActionEvent actionEvent) {
        BetterEDT.switchToSalleSchedule();
    }
}