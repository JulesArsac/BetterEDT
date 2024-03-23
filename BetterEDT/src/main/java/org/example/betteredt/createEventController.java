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
import javafx.scene.layout.ColumnConstraints;
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

public class createEventController implements Initializable {

    public TextField startTimeFieldHeure;
    public TextField startTimeFieldMinute;
    public TextField endTimeFieldHeure;
    public TextField endTimeFieldMinute;
    public AnchorPane rootPane;

    @FXML
    private TextField eventNameField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField locationField;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField dayField;
    @FXML
    private TextField monthField;
    @FXML
    private TextField yearField;

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

    public void addNewEvent(ActionEvent actionEvent) throws SQLException {

        if (eventNameField.getText().isEmpty()||dayField.getText().isEmpty()||monthField.getText().isEmpty()||yearField.getText().isEmpty()||
                startTimeFieldHeure.getText().isEmpty() || startTimeFieldMinute.getText().isEmpty()||endTimeFieldHeure.getText().isEmpty() || endTimeFieldMinute.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        //test des dates
        try {
            int day = Integer.parseInt(dayField.getText());
            int month = Integer.parseInt(monthField.getText());
            int year = Integer.parseInt(yearField.getText());
            LocalDate date = LocalDate.of(year, month, day);
        } catch (DateTimeException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Date invalide");
            alert.setHeaderText(null);
            alert.setContentText("La date saisie n'est pas valide. Veuillez saisir une date valide.");
            alert.showAndWait();
            return;
        }

        try {
            int heureDebut = Integer.parseInt(startTimeFieldHeure.getText());
            int minuteDebut = Integer.parseInt(startTimeFieldMinute.getText());
            int heureFin = Integer.parseInt(endTimeFieldHeure.getText());
            int minuteFin = Integer.parseInt(endTimeFieldMinute.getText());

            if (heureDebut>23||heureFin>23||heureDebut<0||heureFin<0){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Heures invalide");
                alert.setHeaderText(null);
                alert.setContentText("Les heures saisies ne sont pas valide. Veuillez saisir des heures valide.");
                alert.showAndWait();
                return;
            }
            if (minuteDebut!=30){
                if (minuteDebut!=0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Minutes invalide");
                    alert.setHeaderText(null);
                    alert.setContentText("Les minutes de début saisies ne sont pas valide. Veuillez saisir 0 ou 30.");
                    alert.showAndWait();
                    return;
                }
                if (Objects.equals(startTimeFieldMinute.getText(), "0")){
                    startTimeFieldMinute.setText("00");
                }
            }
            if (minuteFin!=30){
                if (minuteFin!=0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Minutes invalide");
                    alert.setHeaderText(null);
                    alert.setContentText("Les minutes de début saisies ne sont pas valide. Veuillez saisir 0 ou 30.");
                    alert.showAndWait();
                    return;
                }
                if (Objects.equals(endTimeFieldMinute.getText(), "0")){
                    endTimeFieldMinute.setText("00");
                }
            }

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Horaires invalide");
            alert.setHeaderText(null);
            alert.setContentText("Les horaires saisies ne sont pas valide. Veuillez saisir des horaires valide.");
            alert.showAndWait();
            return;
        }

        Color selectedColor = colorPicker.getValue();
        String hexColor = String.format("%02X%02X%02X",
                (int) (selectedColor.getRed() * 255),
                (int) (selectedColor.getGreen() * 255),
                (int) (selectedColor.getBlue() * 255));
        System.out.println(hexColor);


        String insertSQL =
                "INSERT INTO personalSchedule(user, eventName, description, lieu, couleur, jour, mois, anner, startHeure, endHeure) " +
                        "VALUES ('" + getUser().getUsername() + "', '" + eventNameField.getText() + "', '" + descriptionArea.getText() + "', '" + locationField.getText() + "', '" + hexColor + "', " +
                        "'" + dayField.getText() + "', '" + monthField.getText() + "', '" + yearField.getText() + "', '" + startTimeFieldHeure.getText() + "H" + startTimeFieldMinute.getText() + "', '" + endTimeFieldHeure.getText() + "H" + endTimeFieldMinute.getText() + "')";

        getConn().createStatement().execute(insertSQL);

        BetterEDT.goToPersonalScreen();

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
