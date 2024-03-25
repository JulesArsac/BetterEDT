package org.example.betteredt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class filtersController implements Initializable {

    private boolean darkMode = false;
    private IEdtController parentController;

    @FXML
    ToggleButton darkSasuke;
    @FXML
    private ComboBox periodChoice;
    @FXML
    public Button addNewReservationEvent;
    @FXML
    public Button addNewPersonalEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (BetterEDT.isDarkMode()) {
            darkSasuke.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: #FFFFFF; -fx-border-color: #222222");
        } else {
            darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");
        }

        periodChoice.getItems().addAll("Semaine" , "Jour" , "Mois");
        periodChoice.getSelectionModel().selectFirst();
        periodChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == oldVal) {
                return;
            }
            if (newVal.equals("Semaine")) {
                parentController.switchToWeeklyFilter();
                BetterEDT.setPrefTime(1);
            }
            else if (newVal.equals("Jour")) {
                parentController.switchToDailyFilter();
                BetterEDT.setPrefTime(0);
            }
            else {
                parentController.switchToMonthlyFilter();
                BetterEDT.setPrefTime(2);
            }
        });
    }

    @FXML
    protected void onDarkSasukeClick() {
        if (!darkMode) {
            BetterEDT.goDarkMode();
            setDarkMode(true);
        } else {
            BetterEDT.goLightMode();
            setDarkMode(false);
        }
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        if (!darkMode) {
            darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");
        } else {
            darkSasuke.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: #FFFFFF; -fx-border-color: #222222");
        }
    }

    public void setParentController(IEdtController parentController) {
        this.parentController = parentController;
    }

    public void setPeriodChoice(int type) {
        switch (type) {
            case 0:
                periodChoice.getSelectionModel().select("Jour");
                break;
            case 1:
                periodChoice.getSelectionModel().select("Semaine");
                break;
            case 2:
                periodChoice.getSelectionModel().select("Mois");
                break;
        }
    }

    public void switchToReservationMenu(ActionEvent actionEvent) {
        BetterEDT.switchToReservationEventMenu();
    }

    public void setReservationVisibility(boolean visible) {
        addNewReservationEvent.setVisible(visible);
    }

    public void switchToCustomEventMenu(ActionEvent actionEvent) {
        BetterEDT.switchToCustomEventMenu();
    }

    public void setCustomEventVisibility(boolean visible) {
        addNewPersonalEvent.setVisible(visible);
    }

}
