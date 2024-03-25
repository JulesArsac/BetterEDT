package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

import static org.example.betteredt.BetterEDT.getConn;
import static org.example.betteredt.BetterEDT.getUser;

public class createEventController implements Initializable {

    private List<EventCalendrier> EventsListSalle = null;

    public TextField startTimeFieldHeure;
    public TextField startTimeFieldMinute;
    public TextField endTimeFieldHeure;
    public TextField endTimeFieldMinute;
    public AnchorPane rootPane;
    public Label salleNameField;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (BetterEDT.isDarkMode()) {
            darkSasuke.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: #FFFFFF; -fx-border-color: #222222");
        } else {
            darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");
        }

        mainGrid.prefHeightProperty().bind(Bindings.subtract(rootPane.heightProperty(), 100));
        if (salleNameField != null){
            salleNameField.setText("S1 = C 042 Nodes (HARDCODED)");
        }

        EventsListSalle = Parser.startParser("src/main/resources/SalleNode.ics");
        if (EventsListSalle == null) {
            throw new RuntimeException("Error while parsing the file");
        }

        Set<EventCalendrier> eventSet = new HashSet<>(EventsListSalle);

        EventsListSalle = new ArrayList<>(eventSet);

        EventsListSalle.sort(new Comparator<EventCalendrier>() {
            @Override
            public int compare(EventCalendrier e1, EventCalendrier e2) {
                return e1.getLocalDateTime().compareTo(e2.getLocalDateTime());
            }
        });

    }

    @FXML
    protected void onDarkSasukeClick() {
        if (!BetterEDT.isDarkMode()) {
            BetterEDT.goDarkMode();
            setDarkMode(true);
        } else {
            BetterEDT.goLightMode();
            setDarkMode(false);
        }
    }

    public void setDarkMode(boolean darkMode) {
        if (!darkMode) {
            darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");
        } else {
            darkSasuke.setStyle("-fx-background-color: #1A1A1A; -fx-text-fill: #FFFFFF; -fx-border-color: #222222");
        }
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

            if (heureDebut>19||heureFin>19||heureDebut<8||heureFin<8){
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
            if (heureDebut>heureFin){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Horaires invalide");
                alert.setHeaderText(null);
                alert.setContentText("Les horaires saisies ne sont pas valide. Veuillez saisir des horaires valide.");
                alert.showAndWait();
                return;
            }
            if (heureDebut==heureFin&&minuteFin<minuteDebut){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Horaires invalide");
                alert.setHeaderText(null);
                alert.setContentText("Les horaires saisies ne sont pas valide. Veuillez saisir des horaires valide.");
                alert.showAndWait();
                return;
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


    public void addNewReservation(ActionEvent actionEvent) throws SQLException {

        if (salleNameField.getText().isEmpty()||dayField.getText().isEmpty()||monthField.getText().isEmpty()||yearField.getText().isEmpty()||
                startTimeFieldHeure.getText().isEmpty() || startTimeFieldMinute.getText().isEmpty()||endTimeFieldHeure.getText().isEmpty() || endTimeFieldMinute.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

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

            if (heureDebut>19||heureFin>19||heureDebut<8||heureFin<8){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Heures invalide");
                alert.setHeaderText(null);
                alert.setContentText("Les heures saisies ne sont pas valide. Veuillez saisir des heures valide.");
                alert.showAndWait();
                return;
            }
            if (heureDebut < 10) {
                startTimeFieldHeure.setText("0" + startTimeFieldHeure.getText());
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
            if (heureFin < 10) {
                endTimeFieldHeure.setText("0" + endTimeFieldHeure.getText());
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
            if (heureDebut>heureFin){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Horaires invalide");
                alert.setHeaderText(null);
                alert.setContentText("Les horaires saisies ne sont pas valide. Veuillez saisir des horaires valide.");
                alert.showAndWait();
                return;
            }
            if (heureDebut==heureFin&&minuteFin<minuteDebut){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Horaires invalide");
                alert.setHeaderText(null);
                alert.setContentText("Les horaires saisies ne sont pas valide. Veuillez saisir des horaires valide.");
                alert.showAndWait();
                return;
            }

            for (int i=0; i<EventsListSalle.size(); i++) {
                if (EventsListSalle.get(i).getJour()==Integer.parseInt(dayField.getText())&&
                        EventsListSalle.get(i).getMois()==Integer.parseInt(monthField.getText())&&
                        EventsListSalle.get(i).getYear()==Integer.parseInt(yearField.getText())){
                    int startHeureReserver = Integer.parseInt(EventsListSalle.get(i).getStartHeure().substring(0, EventsListSalle.get(i).getStartHeure().indexOf('H')));
                    int endHeureReserver = Integer.parseInt(EventsListSalle.get(i).getEndHeure().substring(0, EventsListSalle.get(i).getEndHeure().indexOf('H')));
                    int startMinuteReserver = Integer.parseInt(EventsListSalle.get(i).getStartHeure().substring(EventsListSalle.get(i).getStartHeure().indexOf('H') + 1));
                    int endMinuteReserver = Integer.parseInt(EventsListSalle.get(i).getEndHeure().substring(EventsListSalle.get(i).getEndHeure().indexOf('H') + 1));

                    if (!(endHeureReserver <= heureDebut || startHeureReserver >= heureFin)) {
                        if ((endHeureReserver == heureDebut && endMinuteReserver == minuteFin) ||
                                (startMinuteReserver == minuteFin && startMinuteReserver == minuteDebut)) {
                        } else{
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Créneau déjà pris");
                            alert.setHeaderText(null);
                            alert.setContentText("Les horaires saisies sont déjà réservées. Veuillez saisir des horaires non prises.");
                            alert.showAndWait();
                            return;
                        }
                    }

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


        String insertSQL =
                "INSERT INTO reservationSalleTable(user, salleName, description, jour, mois, anner, startHeure, endHeure) " +
                        "VALUES ('" + getUser().getUsername() + "', '" + salleNameField.getText() + "', '" + descriptionArea.getText() + "', '" +
                        dayField.getText() + "', '" + monthField.getText() + "', '" + yearField.getText() + "', '" +
                        startTimeFieldHeure.getText() + "H" + startTimeFieldMinute.getText() + "', '" +
                        endTimeFieldHeure.getText() + "H" + endTimeFieldMinute.getText() + "')";

        getConn().createStatement().execute(insertSQL);

        BetterEDT.switchToSalleSchedule();

    }
    public void switchToPersonalSchedule(ActionEvent actionEvent) {
        BetterEDT.goToPersonalScreen();
    }

    public void switchToMainScreen(ActionEvent actionEvent) {
        BetterEDT.goToFormationScreen();
    }

    public void switchToSalleSchedule(ActionEvent actionEvent) {
        BetterEDT.switchToSalleSchedule();
    }

    public void setSalleNameField(String salleName) {
        salleNameField.setText(salleName);
    }

}
