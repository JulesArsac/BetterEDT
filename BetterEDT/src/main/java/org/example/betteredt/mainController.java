package org.example.betteredt;

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
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class mainController implements Initializable {

    private List<EventCalendrier> mainList = null;
    @FXML
    private Label welcomeText;
    @FXML
    private ComboBox periodChoice;
    @FXML
    private ToggleButton darkSasuke;
    @FXML
    Pane edtPane;
    @FXML
    AnchorPane rootPane;
    @FXML
    GridPane filterPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        periodChoice.getItems().addAll("Month" , "Week" , "Day");
        periodChoice.getSelectionModel().selectFirst();
        periodChoice.valueProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("Selected period: " + newVal);
        });

        darkSasuke.setStyle("-fx-background-color: #FFFFFF; -fx-text-fill: #000000; -fx-border-color: #222222");


        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            edtPane.setPrefWidth(newVal.doubleValue()-160);
            edtPane.setMaxWidth(newVal.doubleValue()-160);
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
            edtPane.setPrefHeight(newVal.doubleValue()-129);
            edtPane.setMaxHeight(newVal.doubleValue()-129);

            filterPane.setPrefHeight(newVal.doubleValue()-150);
            filterPane.setMaxHeight(newVal.doubleValue()-150);
        });

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("weeklyGrid.fxml"));
        Parent rootNode = null;
        try {
            rootNode = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        AnchorPane edtRootPane = (AnchorPane) rootNode.lookup("#rootPane");

        edtRootPane.prefWidthProperty().bind(edtPane.widthProperty());
        edtRootPane.prefHeightProperty().bind(edtPane.heightProperty());
        edtRootPane.maxWidthProperty().bind(edtPane.widthProperty());
        edtRootPane.maxHeightProperty().bind(edtPane.heightProperty());
        edtRootPane.minWidthProperty().bind(edtPane.widthProperty());
        edtRootPane.minHeightProperty().bind(edtPane.heightProperty());

        edtPane.getChildren().add(rootNode);



        LocalDate currentDate = LocalDate.now();

        LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));



        weeklyGridController controller = fxmlLoader.getController();
        controller.setWeeklyList(getEvents(startOfWeek, endOfWeek));


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
        BetterEDT.goToPersonalScreen();
    }

    public void switchToMainScreen(ActionEvent actionEvent) {
        BetterEDT.goToMainScreen();
    }

    public void switchToSalleSchedule(ActionEvent actionEvent) {
        BetterEDT.switchToSalleSchedule();
    }

    public void setupMainList() {
        mainList = Parser.startParser("src/main/resources/ILSEN.ics");
        if (mainList == null) {
            throw new RuntimeException("Error while parsing the file");
        }

        Set<EventCalendrier> eventSet = new HashSet<>(mainList);

        mainList = new ArrayList<>(eventSet);

        mainList.sort(new Comparator<EventCalendrier>() {
            @Override
            public int compare(EventCalendrier e1, EventCalendrier e2) {
                return e1.getLocalDateTime().compareTo(e2.getLocalDateTime());
            }
        });
    }

    public List<List<EventCalendrier>> getEvents(LocalDate startDate, LocalDate endDate) {
        List<List<EventCalendrier>> eventList = new ArrayList<>();
        boolean start = false;
        LocalDate savedDate = startDate;
        int currentIndex = 0;
        eventList.add(new ArrayList<EventCalendrier>());
        for (EventCalendrier event : mainList) {
            LocalDate eventDate = LocalDate.of(event.getYear(), event.getMois(), event.getJour());
            if (eventDate.isAfter(endDate)) {
                break;
            }
            if (event.getJour() == startDate.getDayOfMonth() && event.getMois() == startDate.getMonthValue() && event.getYear() == startDate.getYear()) {
                start = true;
            }
            if (eventDate.isAfter(savedDate)) {
                savedDate = eventDate;
                currentIndex++;
                eventList.add(new ArrayList<EventCalendrier>());
            }
            if (start) {
                eventList.get(currentIndex).add(event);
            }

        }
        return eventList;
    }

}