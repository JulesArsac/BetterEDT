package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
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

        edtGrid.setGridLinesVisible(true);

        for (int i= 0; i < 5; i++) {
            Label label = new Label(i + "");
            edtGrid.add(label, i, 0);
        }

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);

        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPercentWidth(20);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(20);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(20);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(20);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setPercentWidth(20);

        edtGrid.getRowConstraints().addAll(row1, row2);
        edtGrid.getColumnConstraints().addAll(col0, col1, col2, col3, col4);

        //Code that add 5 vbox to the grid. Within each vbox, there are 23 regions (to represent 30min intervals). 3 of them are blue rectangles.
//        for (int j = 0; j < 5; j++) {
//            VBox vbox = setUpVBox();
//
//            for (int i = 0; i < 23; i++) {
//                if (i == 5 || i == 12 || i == 22) {
//                    Rectangle blueRectangle = new Rectangle();
//                    blueRectangle.widthProperty().bind(vbox.prefWidthProperty());
//                    blueRectangle.heightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
//                    blueRectangle.setFill(Color.BLUE);
//                    vbox.getChildren().add(blueRectangle);
//                    System.out.println("Added blue rectangle");
//                } else {
//                    Region spacer = new Region();
//                    spacer.prefHeightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
//                    vbox.getChildren().add(spacer);
//                }
//            }
//            edtGrid.add(vbox, j, 1);
//        }

        if (weeklyList != null) {
            LocalTime startTime = LocalTime.of(8, 30);
            LocalTime endTime = LocalTime.of(19, 0);
            Duration increment = Duration.ofMinutes(30);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'H'mm");
            int column = 0;
            int currentDuration = 0;
            for (List<EventCalendrier> dayEventList : weeklyList) {
                LocalTime currentTime = startTime;
                VBox vbox = setUpVBox();
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
                            eventController controller = fxmlLoader.getController();
                            controller.setTime(event.getStartHeure() + " - " + event.getEndHeure());
                            controller.setRoom(event.getLocation());
                            controller.setTitle(event.getUCE());
                            controller.setProf(event.getProfesseur());
                            controller.setType(event.getTypeDeCours());
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
                column++;
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
