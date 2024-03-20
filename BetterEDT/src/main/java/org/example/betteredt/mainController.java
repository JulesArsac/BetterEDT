package org.example.betteredt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class mainController implements Initializable {
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

    @Override
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
        else {

            mainList.sort(new Comparator<EventCalendrier>() {
                @Override
                public int compare(EventCalendrier e1, EventCalendrier e2) {
                    return e1.getLocalDateTime().compareTo(e2.getLocalDateTime());
                }
            });

            LocalDate currentDate = LocalDate.now();

            int day = currentDate.getDayOfMonth();
            int month = currentDate.getMonthValue();
            int year = currentDate.getYear();

            System.out.println("Current Day: " + day);
            System.out.println("Current Month: " + month);
            System.out.println("Current Year: " + year);

            LocalDate startOfWeek = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate endOfWeek = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

            int startDay = startOfWeek.getDayOfMonth();
            int startMonth = startOfWeek.getMonthValue();
            int startYear = startOfWeek.getYear();

            int endDay = endOfWeek.getDayOfMonth();
            int endMonth = endOfWeek.getMonthValue();
            int endYear = endOfWeek.getYear();

            System.out.println("Start of Week: " + startDay + "-" + startMonth + "-" + startYear);
            System.out.println("End of Week: " + endDay + "-" + endMonth + "-" + endYear);


            LocalDate startOfMonth = currentDate.with(TemporalAdjusters.firstDayOfMonth());

            // Get the end of the month (last day)
            LocalDate endOfMonth = currentDate.with(TemporalAdjusters.lastDayOfMonth());

            int startDayMonth = startOfMonth.getDayOfMonth();
            int startMonthMonth = startOfMonth.getMonthValue();
            int startYearMonth = startOfMonth.getYear();

            int endDayMonth = endOfMonth.getDayOfMonth();
            int endMonthMonth = endOfMonth.getMonthValue();
            int endYearMonth = endOfMonth.getYear();

            System.out.println("Start of Month: " + startDayMonth + "-" + startMonthMonth + "-" + startYearMonth);
            System.out.println("End of Month: " + endDayMonth + "-" + endMonthMonth + "-" + endYearMonth);

            LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);
            LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

            List<List<EventCalendrier>> eventList = new ArrayList<>();
            boolean start = false;
            LocalDate savedDate = startDate;
            int currentIndex = 0;
            eventList.set(currentIndex, new ArrayList<EventCalendrier>());
            for (EventCalendrier event : mainList) {
                LocalDate eventDate = LocalDate.of(event.getYear(), event.getMois(), event.getJour());
                if (eventDate.isAfter(endDate)) {
                    break;
                }
                if (event.getJour() == day && event.getMois() == month && event.getYear() == year) {
                    start = true;
                }
                if (eventDate.isAfter(savedDate)) {
                    savedDate = eventDate;
                    currentIndex++;
                    eventList.set(currentIndex, new ArrayList<EventCalendrier>());
                }
                if (start) {
                    eventList.get(currentIndex).add(event);
                    System.out.println("Event start date:" + event.getJour() + "-" + event.getMois() + "-" + event.getYear() + " " + event.getStartHeure() + " - " + event.getEndHeure());
                }

            }
            LocalTime startTime = LocalTime.of(8, 0); // 08:00
            LocalTime endTime = LocalTime.of(19, 30); // 19:30
            Duration increment = Duration.ofMinutes(30);

            int i = 0;
            for (List<EventCalendrier> dayEventList : eventList) {
                //TODO: process the events
//                LocalTime currentTime = startTime;
//                while (currentTime.isBefore(endTime)) {
//                    System.out.println(i);
//                    i++;
//                    currentTime = currentTime.plus(increment);
//                }
            }
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
}