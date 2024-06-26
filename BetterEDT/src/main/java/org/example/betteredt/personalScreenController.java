package org.example.betteredt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class personalScreenController implements Initializable, IEdtController {

    private List<EventCalendrier> allEventList = null;
    private List<EventCalendrier> mainList = null;
    private LocalDate displayedDate = LocalDate.now();
    private int currentDisplay = 1;
    private filtersController filtersController;

    @FXML
    Pane edtPane;
    @FXML
    AnchorPane rootPane;
    @FXML
    Pane filterPane;
    @FXML
    VBox middleVbox;
    @FXML
    Label currentDateLabel;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            edtPane.setPrefWidth(newVal.doubleValue()-160);
            edtPane.setMaxWidth(newVal.doubleValue()-160);

            middleVbox.setPrefWidth(newVal.doubleValue());

            if (newVal.doubleValue() < 180) {
                filterPane.setPrefWidth(newVal.doubleValue()-40);
                filterPane.setMaxWidth(newVal.doubleValue()-40);
            }
            else {
                filterPane.setPrefWidth(140);
                filterPane.setMaxWidth(140);
            }
        });
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            edtPane.setPrefHeight(newVal.doubleValue()-129);
            edtPane.setMaxHeight(newVal.doubleValue()-129);

            filterPane.setPrefHeight(newVal.doubleValue()-150);
            filterPane.setMaxHeight(newVal.doubleValue()-150);
        });

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("filters.fxml"));
        VBox rootNode = null;
        try {
            rootNode = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        filtersController = fxmlLoader.getController();
        filtersController.setParentController(this);
        filtersController.setCustomEventVisibility(true);

        rootNode.prefWidthProperty().bind(filterPane.widthProperty());
        rootNode.prefHeightProperty().bind(filterPane.heightProperty());

        filterPane.getChildren().add(rootNode);

        String savedIcs = BetterEDT.getSavedFile();
        if (savedIcs != null) {
            setupMainList("src/main/resources/personnel/" + savedIcs);
        }
        else {
            setupMainList("src/main/resources/personnel/ILSENcla.ics");
        }

        switchToWeekly(LocalDate.now());

        rootPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.LEFT) {
                goBack();
            }
            else if (event.getCode() == KeyCode.RIGHT) {
                goForward();
            }
        });
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

    public void setupMainList(String path) {
        allEventList = BetterEDT.parseFile(path);
        if (allEventList == null) {
            throw new RuntimeException("Error while parsing the file");
        }

        allEventList = BetterEDT.addUserEvent(allEventList);

        Set<EventCalendrier> eventSet = new HashSet<>(allEventList);

        allEventList = new ArrayList<>(eventSet);

        allEventList.sort(new Comparator<EventCalendrier>() {
            @Override
            public int compare(EventCalendrier e1, EventCalendrier e2) {
                return e1.getLocalDateTime().compareTo(e2.getLocalDateTime());
            }
        });
        mainList = allEventList;
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
            if (eventDate.isAfter(startDate) || eventDate.equals(startDate)) {
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

    public List<monthEvent> getMonthlyList(LocalDate startDate, LocalDate endDate) {
        List<monthEvent> eventList = new ArrayList<>();
        LocalDate currentDate = startDate;
        boolean eval = false;
        int nbEvent = 0;
        while (currentDate.isBefore(endDate)) {
            for (EventCalendrier event : mainList) {
                LocalDate eventDate = LocalDate.of(event.getYear(), event.getMois(), event.getJour());
                if (eventDate.isAfter(endDate)) {
                    break;
                }
                if (event.getJour() == currentDate.getDayOfMonth() && event.getMois() == currentDate.getMonthValue() && event.getYear() == currentDate.getYear()) {
                    if (event.getTypeDeCours().equals("Evaluation")) {
                        eval = true;
                    }
                    nbEvent++;
                }
            }
            monthEvent newEvent = new monthEvent();
            newEvent.setNbEvent(nbEvent);
            newEvent.setEval(eval);
            eventList.add(newEvent);
            nbEvent = 0;
            currentDate = currentDate.plusDays(1);
            eval = false;
        }
        return eventList;
    }

    private void switchToWeekly(LocalDate weekDay) {
        edtPane.getChildren().clear();

        for (EventCalendrier event : mainList) {
            event.setDisplayed(false);
        }

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

        LocalDate startOfWeek = weekDay.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = weekDay.with(DayOfWeek.SUNDAY);

        currentDateLabel.setText("Semaine du " + startOfWeek.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " au " + endOfWeek.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        weeklyGridController controller = fxmlLoader.getController();
        controller.setWeeklyList(getEvents(startOfWeek, endOfWeek));

        edtPane.getChildren().add(rootNode);
    }

    public void switchToDaily(LocalDate dayDate) {
        edtPane.getChildren().clear();

        for (EventCalendrier event : mainList) {
            event.setDisplayed(false);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dailyGrid.fxml"));
        Parent rootNode = null;
        try {
            rootNode = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GridPane edtRootPane = (GridPane) rootNode.lookup("#edtGrid");
        edtRootPane.prefWidthProperty().bind(edtPane.widthProperty().divide(2));
        edtRootPane.prefHeightProperty().bind(edtPane.heightProperty());
        edtRootPane.maxWidthProperty().bind(edtPane.widthProperty().divide(2));
        edtRootPane.maxHeightProperty().bind(edtPane.heightProperty());
        edtRootPane.minWidthProperty().bind(edtPane.widthProperty().divide(2));
        edtRootPane.minHeightProperty().bind(edtPane.heightProperty());


        currentDateLabel.setText("Journée du " + dayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        dailyGridController controller = fxmlLoader.getController();
        controller.setDate(dayDate);
        controller.setEventList(getEvents(dayDate, dayDate).get(0));

        edtPane.getChildren().add(rootNode);
    }

    public void switchToMonthly(LocalDate dayOfMonth) {
        edtPane.getChildren().clear();

        for (EventCalendrier event : mainList) {
            event.setDisplayed(false);
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("monthlyGrid.fxml"));
        Parent rootNode = null;
        try {
            rootNode = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GridPane edtRootPane = (GridPane) rootNode.lookup("#edtGrid");
        edtRootPane.prefWidthProperty().bind(edtPane.widthProperty());
        edtRootPane.prefHeightProperty().bind(edtPane.heightProperty());
        edtRootPane.maxWidthProperty().bind(edtPane.widthProperty());
        edtRootPane.maxHeightProperty().bind(edtPane.heightProperty());
        edtRootPane.minWidthProperty().bind(edtPane.widthProperty());
        edtRootPane.minHeightProperty().bind(edtPane.heightProperty());


        LocalDate startOfMonth = dayOfMonth.with(TemporalAdjusters.firstDayOfMonth());

        DayOfWeek dayOfWeek = startOfMonth.getDayOfWeek();

        currentDateLabel.setText("Mois de " + startOfMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));

        monthlyGridController controller = fxmlLoader.getController();
        controller.setFirstDay(dayOfWeek);
        controller.setEventNums(getMonthlyList(startOfMonth, startOfMonth.with(TemporalAdjusters.lastDayOfMonth())));

        edtPane.getChildren().add(rootNode);
    }

    public void goBack() {
        switch (currentDisplay) {
            case 0:
                displayedDate = displayedDate.minusDays(1);
                switchToDaily(displayedDate);
                break;
            case 1:
                displayedDate = displayedDate.minusWeeks(1);
                switchToWeekly(displayedDate);
                break;
            case 2:
                displayedDate = displayedDate.minusMonths(1);
                switchToMonthly(displayedDate);
                break;
        }
    }

    public void goForward() {
        switch (currentDisplay) {
            case 0:
                displayedDate = displayedDate.plusDays(1);
                switchToDaily(displayedDate);
                break;
            case 1:
                displayedDate = displayedDate.plusWeeks(1);
                switchToWeekly(displayedDate);
                break;
            case 2:
                displayedDate = displayedDate.plusMonths(1);
                switchToMonthly(displayedDate);
                break;
        }
    }

    public void changePrefType(int type) {
        filtersController.setPeriodChoice(type);
    }

    public void selectFormation() {
        String path = BetterEDT.getIcsName(2);
        if (path == null) {
            return;
        }
        setupMainList(path);
        if (currentDisplay == 0) {
            switchToDaily(displayedDate);
        }
        else if (currentDisplay == 1) {
            switchToWeekly(displayedDate);
        }
        else {
            switchToMonthly(displayedDate);
        }
    }

    public void switchToDailyFilter() {
        currentDisplay = 0;
        switchToDaily(displayedDate);
    }

    public void switchToWeeklyFilter() {
        currentDisplay = 1;
        switchToWeekly(displayedDate);
    }

    public void switchToMonthlyFilter() {
        currentDisplay = 2;
        switchToMonthly(displayedDate);
    }

    @Override
    public void updateEventList(List<EventCalendrier> newEvents) {
        mainList = newEvents;
        switch (currentDisplay) {
            case 0:
                switchToDaily(displayedDate);
                break;
            case 1:
                switchToWeekly(displayedDate);
                break;
            case 2:
                switchToMonthly(displayedDate);
                break;
        }
    }

    @Override
    public List<EventCalendrier> getCurrentEvents() {
        return allEventList;
    }

}