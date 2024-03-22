package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
            LocalTime startTime = LocalTime.of(8, 0); // 08:00
            LocalTime endTime = LocalTime.of(19, 30); // 19:30
            Duration increment = Duration.ofMinutes(30);

//            for (List<EventCalendrier> dayEventList : weeklyList) {
//                for (EventCalendrier event : dayEventList) {
//                    System.out.println("Event: " + event.getSummary() + " - " + event.getStartHeure() + " - " + event.getEndHeure() + " - " + event.getLocation() + " - " + event.getMois() + "/" + event.getJour() + "/" + event.getYear() + " - " + event.getJourSemaine() + " - " + event.getAdditionalInfo() + " - " + event.getProfesseur() + " - " + event.getUCE() + " - " + event.getTypeDeCours() + " - " + event.getElevesConcerner());
//                }
//                System.out.println("===================================== NEW DAY =====================================");
//            }


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH'H'mm");
            for (List<EventCalendrier> dayEventList : weeklyList) {
                LocalTime currentTime = startTime;
                while (currentTime.isBefore(endTime)) { //Loop from 08:00 to 19:30
                    for (EventCalendrier event : dayEventList) {
                        if (event.isDisplayed()) {
                            continue;
                        }
                        LocalTime eventStartTime = LocalTime.parse(event.getStartHeure(), formatter);
                        if (currentTime.equals(eventStartTime)) {
                            System.out.println(event.getStartHeure() + " - " + event.getEndHeure() + " - " + event.getLocation() + " - " + event.getMois() + "/" + event.getJour() + "/" + event.getYear() + " - " + event.getJourSemaine());
                            event.setDisplayed(true);
                        }
                    }

                    currentTime = currentTime.plus(increment);
                }
                System.out.println("===================================== NEW DAY =====================================");
            }
        }

        LocalTime start = LocalTime.of(8, 30);
        LocalTime end = LocalTime.of(11, 30);

        TimeSpan ts = new TimeSpan(start, end);
        LocalTime test = LocalTime.of(9, 0);
        LocalTime test2 = LocalTime.of(8, 30);
        LocalTime test3 = LocalTime.of(11, 30);
        System.out.println(ts.contains(test));
        System.out.println(ts.contains(test2));
        System.out.println(ts.contains(test3));

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
