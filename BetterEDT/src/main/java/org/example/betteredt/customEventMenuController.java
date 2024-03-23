package org.example.betteredt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class customEventMenuController {

    @FXML
    private TextField eventNameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField locationField;
    @FXML
    private TextField colorField;
    @FXML
    private TextField dayField;
    @FXML
    private TextField monthField;
    @FXML
    private TextField yearField;
    @FXML
    private TextField startTimeField;
    @FXML
    private TextField endTimeField;

    @FXML
    private Button sendButton;
    @FXML
    private ToggleButton darkSasuke;

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


    public void addNewEvent(ActionEvent actionEvent) {
        System.out.println("Event Name: " + eventNameField.getText());
        System.out.println("Description: " + descriptionArea.getText());
        System.out.println("Location: " + locationField.getText());
        System.out.println("Color: " + colorField.getText());
        System.out.println("Day: " + dayField.getText());
        System.out.println("Month: " + monthField.getText());
        System.out.println("Year: " + yearField.getText());
        System.out.println("Start Time: " + startTimeField.getText());
        System.out.println("End Time: " + endTimeField.getText());


        if (eventNameField.getText().isEmpty()||dayField.getText().isEmpty()||monthField.getText().isEmpty()||yearField.getText().isEmpty()||
                startTimeField.getText().isEmpty() || endTimeField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
        }
        //faire les tests et verifier si l'utilisteur remplie bien les fields puis insert into
        BetterEDT.goToPersonalScreen();

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
