package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class weeklyGridController implements Initializable {


    List<List<EventCalendrier>> weeklyList = null;
    @FXML
    AnchorPane rootPane;
    @FXML
    GridPane edtGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        edtGrid.getChildren().clear();
        edtGrid.getRowConstraints().clear();
        edtGrid.getColumnConstraints().clear();

        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            edtGrid.setPrefWidth(newVal.doubleValue());
            edtGrid.setMaxWidth(newVal.doubleValue());
        });
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            edtGrid.setPrefHeight(newVal.doubleValue());
            edtGrid.setMaxHeight(newVal.doubleValue());
        });

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);

        edtGrid.add(new Label("Lundi"), 0, 0);
        edtGrid.add(new Label("Mardi"), 1, 0);
        edtGrid.add(new Label("Mercredi"), 2, 0);
        edtGrid.add(new Label("Jeudi"), 3, 0);
        edtGrid.add(new Label("Vendredi"), 4, 0);



        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(20);
        col0.setHalignment(HPos.CENTER);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        col1.setHalignment(HPos.CENTER);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        col2.setHalignment(HPos.CENTER);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        col3.setHalignment(HPos.CENTER);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(20);
        col4.setHalignment(HPos.CENTER);

        edtGrid.getRowConstraints().addAll(row1, row2);
        edtGrid.getColumnConstraints().addAll(col0, col1, col2, col3, col4);


        if (weeklyList != null) {
            LocalTime startTime = LocalTime.of(8, 30);
            LocalTime endTime = LocalTime.of(19, 0);
            Duration increment = Duration.ofMinutes(30);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'H'mm");
            if (weeklyList.isEmpty()) {
                return;
            }


            int currentDuration = 0;
            for (List<EventCalendrier> dayEventList : weeklyList) {
                if (dayEventList.isEmpty()) {
                    continue;
                }
                LocalDate firstEventDate = LocalDate.of(dayEventList.get(0).getYear(), dayEventList.get(0).getMois(), dayEventList.get(0).getJour());
                int column = firstEventDate.getDayOfWeek().getValue() - 1;
                LocalTime currentTime = startTime;
                VBox vbox = setUpVBox();
                vbox.setStyle("-fx-border-color: black;");
                edtGrid.add(vbox, column, 1);
                List<HBox> hboxList = new ArrayList<>();
                TimeSpan span = new TimeSpan(LocalTime.of(0, 0), LocalTime.of(0, 0));
                while (currentTime.isBefore(endTime)) { //Loop from 08:30 to 19:00
                    List<EventCalendrier> eventToDisplay = new ArrayList<>();
                    TimeSpan spanToDisplay = new TimeSpan(LocalTime.of(0, 0), LocalTime.of(0, 0));
                    for (EventCalendrier event : dayEventList) {
                        if (event.isDisplayed()) {
                            continue;
                        }
                        LocalTime eventStartTime = LocalTime.parse(event.getStartHeure(), formatter);
                        LocalTime eventEndTime = LocalTime.parse(event.getEndHeure(), formatter);
                        if (currentTime.equals(eventStartTime) && spanToDisplay.getStart().equals(LocalTime.of(0, 0)) && spanToDisplay.getEnd().equals(LocalTime.of(0, 0))) {
                            spanToDisplay.setStart(eventStartTime);
                            spanToDisplay.setEnd(eventEndTime);
                            eventToDisplay.add(event);
                            event.setDisplayed(true);
                        }
                        else if (currentTime.equals(eventStartTime) || spanToDisplay.contains(eventStartTime)) {
                            if (spanToDisplay.getEnd().isBefore(eventEndTime)) {
                                spanToDisplay.setEnd(eventEndTime);
                            }
                            eventToDisplay.add(event);
                            event.setDisplayed(true);
                        }

                    }
                    if (eventToDisplay.isEmpty() && !span.contains(currentTime)) {
                        Region spacer = new Region();
                        spacer.prefHeightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
                        spacer.minHeightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
                        spacer.maxHeightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
                        vbox.getChildren().add(spacer);
                    } else {
                        int biggestDuration = 0;
                        for (EventCalendrier event : eventToDisplay) {
                            LocalTime eventStartTime = LocalTime.parse(event.getStartHeure(), formatter);
                            LocalTime eventEndTime = LocalTime.parse(event.getEndHeure(), formatter);
                            int duration = (int) Math.ceil((double) Duration.between(eventStartTime, eventEndTime).toMinutes() / 30);
                            if (duration > biggestDuration) {
                                biggestDuration = duration;
                                if (span.getStart().equals(LocalTime.of(0, 0)) && span.getEnd().equals(LocalTime.of(0, 0))) {
                                    span = new TimeSpan(eventStartTime, eventEndTime);
                                    currentDuration = duration;
                                } else {
                                    if (eventStartTime.isAfter(span.getEnd()) || eventStartTime.equals(span.getEnd())) {
                                        span.setStart(eventStartTime);
                                        span.setEnd(eventEndTime);
                                        currentDuration = (int) Math.ceil((double) Duration.between(span.getStart(), eventEndTime).toMinutes() / 30);
                                    }
                                    else {
                                        span.setEnd(eventEndTime);
                                        currentDuration = (int) Math.ceil((double) Duration.between(span.getStart(), span.getEnd()).toMinutes() / 30);
                                    }
                                }
                            }
                        }
                        if (!spanToDisplay.getStart().equals(LocalTime.of(0, 0)) && !spanToDisplay.getEnd().equals(LocalTime.of(0, 0))) {
                            span = spanToDisplay;
                            currentDuration = (int) Math.ceil((double) Duration.between(span.getStart(), span.getEnd()).toMinutes() / 30);
                        }
                        biggestDuration = (int) Math.ceil((double) Duration.between(span.getStart(), span.getEnd()).toMinutes() / 30);
                        if (!span.contains(currentTime)) { //New event group
                            HBox hbox = new HBox();
                            hbox.prefHeightProperty().bind(Bindings.multiply(Bindings.divide(vbox.heightProperty(), 23), biggestDuration));
                            hbox.maxHeightProperty().bind(Bindings.multiply(Bindings.divide(vbox.heightProperty(), 23), biggestDuration));
                            hbox.setMinHeight(0);
                            hbox.prefWidthProperty().bind(vbox.prefWidthProperty());
                            hbox.setSpacing(0);
                            hboxList.add(hbox);
                            vbox.getChildren().add(hbox);
                        }

                        for (EventCalendrier event : eventToDisplay) {
                            HBox hbox = hboxList.get(hboxList.size() - 1);
                            LocalTime eventStartTime = LocalTime.parse(event.getStartHeure(), formatter);
                            LocalTime eventEndTime = LocalTime.parse(event.getEndHeure(), formatter);
                            int duration = (int) Math.ceil((double) Duration.between(eventStartTime, eventEndTime).toMinutes() / 30);
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("event.fxml"));
                            Pane eventRootNode = null;
                            try {
                                eventRootNode = fxmlLoader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            eventRootNode.prefHeightProperty().bind(Bindings.multiply(Bindings.divide(vbox.heightProperty(), 23), duration));
                            eventRootNode.maxHeightProperty().bind(Bindings.multiply(Bindings.divide(vbox.heightProperty(), 23), duration));
                            eventRootNode.setMinHeight(0);
                            eventRootNode.prefWidthProperty().bind(Bindings.divide(hbox.widthProperty(), eventToDisplay.size()));

                            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("eventTooltip.fxml"));
                            Tooltip tooltip = new Tooltip();
                            try {
                                tooltip.setGraphic(fxmlLoader2.load());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            EventTooltipController tooltipController = fxmlLoader2.getController();
                            tooltipController.setEvent(event);

                            Tooltip.install(eventRootNode, tooltip);

                            tooltip.setShowDelay(javafx.util.Duration.millis(50));
                            tooltip.setHideDelay(javafx.util.Duration.millis(50));
                            Tooltip.install(eventRootNode, tooltip);

                            eventController controller = fxmlLoader.getController();
                            controller.setTime(event.getStartHeure() + " - " + event.getEndHeure());
                            controller.setRoom(event.getLocation());
                            controller.setTitle(event.getUCE());
                            controller.setProf(event.getProfesseur());
                            controller.setType(event.getTypeDeCours());
                            controller.setEvent(event);
                            if (eventStartTime.equals(span.getStart())) {
                                hbox.getChildren().add(eventRootNode);
                            }
                            else {
                                VBox spacerVBox = new VBox();
                                spacerVBox.prefHeightProperty().bind(hbox.heightProperty());
                                spacerVBox.prefWidthProperty().bind(Bindings.divide(hbox.widthProperty(), eventToDisplay.size()));
                                int nbSpacer = currentDuration - duration;
                                for (int i = 0; i < nbSpacer; i++) { //Add spacers to fill the gap
                                    Region spacer = new Region();
                                    spacer.prefHeightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
                                    spacerVBox.getChildren().add(spacer);
                                }
                                spacerVBox.getChildren().add(eventRootNode);
                                hbox.getChildren().add(spacerVBox);
                            }
                        }

                    }

                    currentTime = currentTime.plus(increment);
                }
                //New day
            }
        }

    }

    public void setWeeklyList(List<List<EventCalendrier>> weeklyList) {
        this.weeklyList = weeklyList;
        initialize(null, null);
    }

    public VBox setUpVBox() {
        VBox vbox = new VBox();
        vbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vbox.setMaxHeight(Double.MAX_VALUE);
        vbox.setSpacing(0);

        vbox.prefWidthProperty().bind(edtGrid.widthProperty().divide(5));
        return vbox;
    }

}
