package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.example.betteredt.BetterEDT.getConn;
import static org.example.betteredt.BetterEDT.getUser;

public class createEventController implements Initializable {

    @FXML
    public DatePicker datePicker;
    @FXML
    public ComboBox<String> startTimeHourComboBox;
    @FXML
    public ComboBox<String> startTimeMinuteComboBox;

    @FXML
    public ComboBox<String> endTimeHourComboBox;
    @FXML
    public ComboBox<String> endTimeMinuteComboBox;
    private List<EventCalendrier> EventsListSalle = null;


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
        ObservableList<String> hourOptions = FXCollections.observableArrayList(
                "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
        );
        startTimeHourComboBox.setItems(hourOptions);
        ObservableList<String> minuteOptions = FXCollections.observableArrayList("0", "30");
        startTimeMinuteComboBox.setItems(minuteOptions);

        endTimeHourComboBox.setItems(hourOptions);
        endTimeMinuteComboBox.setItems(minuteOptions);

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
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null || startTimeHourComboBox.getValue() == null || endTimeHourComboBox.getValue() == null ||
                startTimeMinuteComboBox.getValue() == null || endTimeMinuteComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        String dayField= String.valueOf(selectedDate.getDayOfMonth());
        String monthField = String.valueOf(selectedDate.getMonthValue());
        String yearField = String.valueOf(selectedDate.getYear());

        String startTimeFieldHeure=startTimeHourComboBox.getValue();
        String endTimeFieldHeure=endTimeHourComboBox.getValue();
        String startTimeFieldMinute=startTimeMinuteComboBox.getValue();
        String endTimeFieldMinute=endTimeMinuteComboBox.getValue();

        try {
            int heureDebut = Integer.parseInt(startTimeFieldHeure);
            int minuteDebut = Integer.parseInt(startTimeFieldMinute);
            int heureFin = Integer.parseInt(endTimeFieldHeure);
            int minuteFin = Integer.parseInt(endTimeFieldMinute);


            if (heureDebut < 10) {
                startTimeFieldHeure=("0" + startTimeFieldHeure);
            }
            if (Objects.equals(startTimeFieldMinute, "0")){
                startTimeFieldMinute=("00");
            }
            if (heureFin < 10) {
                endTimeFieldHeure=("0" + endTimeFieldHeure);
            }
            if (Objects.equals(endTimeFieldMinute, "0")){
                endTimeFieldMinute="00";
            }

            if (heureDebut>heureFin||heureDebut==19&&minuteDebut==30||heureFin==19&&minuteFin==30){
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
                        "'" + dayField + "', '" + monthField + "', '" + yearField + "', '" + startTimeFieldHeure + "H" + startTimeFieldMinute + "', '" + endTimeFieldHeure + "H" + endTimeFieldMinute + "')";

        getConn().createStatement().execute(insertSQL);

        BetterEDT.goToPersonalScreen();

    }


    public void addNewReservation(ActionEvent actionEvent) throws SQLException {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null || startTimeHourComboBox.getValue() == null || endTimeHourComboBox.getValue() == null ||
                startTimeMinuteComboBox.getValue() == null || endTimeMinuteComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs obligatoires");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs obligatoires.");
            alert.showAndWait();
            return;
        }

        String dayField= String.valueOf(selectedDate.getDayOfMonth());
        String monthField = String.valueOf(selectedDate.getMonthValue());
        String yearField = String.valueOf(selectedDate.getYear());

        String startTimeFieldHeure=startTimeHourComboBox.getValue();
        String endTimeFieldHeure=endTimeHourComboBox.getValue();

        String startTimeFieldMinute=startTimeMinuteComboBox.getValue();
        String endTimeFieldMinute=endTimeMinuteComboBox.getValue();


        try {
            int heureDebut = Integer.parseInt(startTimeFieldHeure);
            int minuteDebut = Integer.parseInt(startTimeFieldMinute);
            int heureFin = Integer.parseInt(endTimeFieldHeure);
            int minuteFin = Integer.parseInt(endTimeFieldMinute);

            if (heureDebut < 10) {
                startTimeFieldHeure=("0" + startTimeFieldHeure);
            }
            if (Objects.equals(startTimeFieldMinute, "0")){
                startTimeFieldMinute=("00");
            }
            if (heureFin < 10) {
                endTimeFieldHeure=("0" + endTimeFieldHeure);
            }
            if (Objects.equals(endTimeFieldMinute, "0")){
                endTimeFieldMinute="00";
            }

            if (heureDebut>heureFin||heureDebut==19&&minuteDebut==30||heureFin==19&&minuteFin==30){
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
                if (EventsListSalle.get(i).getJour()==Integer.parseInt(dayField)&&
                        EventsListSalle.get(i).getMois()==Integer.parseInt(monthField)&&
                        EventsListSalle.get(i).getYear()==Integer.parseInt(yearField)){

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'H'mm");
                    LocalTime startHeureReserver = LocalTime.parse(EventsListSalle.get(i).getStartHeure(), formatter);
                    LocalTime endHeureReserver = LocalTime.parse(EventsListSalle.get(i).getEndHeure(), formatter);

                    LocalTime startHeure = LocalTime.of(heureDebut, minuteDebut);
                    LocalTime endHeure = LocalTime.of(heureFin, minuteFin);

                    TimeSpan oldSpan = new TimeSpan(startHeureReserver, endHeureReserver);
                    if (oldSpan.contains(startHeure) || oldSpan.contains(endHeure) || oldSpan.contains(startHeure) || oldSpan.contains(endHeure) || startHeureReserver.equals(startHeure) || startHeure.isBefore(startHeureReserver)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Créneau déjà pris");
                        alert.setHeaderText(null);
                        alert.setContentText("Les horaires saisies sont déjà réservées. Veuillez saisir des horaires non prises.");
                        alert.showAndWait();
                        return;
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
                        dayField + "', '" + monthField + "', '" + yearField + "', '" +
                        startTimeFieldHeure + "H" + startTimeFieldMinute + "', '" +
                        endTimeFieldHeure + "H" + endTimeFieldMinute + "')";

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
