package org.example.betteredt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class dailyGridController implements Initializable {

    private List<EventCalendrier> eventList = null;
    private LocalDate date = null;

    @FXML
    GridPane edtGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        edtGrid.getChildren().clear();
        edtGrid.getRowConstraints().clear();
        edtGrid.getColumnConstraints().clear();

        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        edtGrid.getRowConstraints().addAll(row1, row2);
    }

    public void setEventList(List<EventCalendrier> eventList) {
        this.eventList = eventList;
        if (date != null) {
            initialize(null, null);
        }
    }

    public void setDate(LocalDate date) {
        this.date = date;
        if (eventList != null) {
            initialize(null, null);
        }
    }

    public GridPane getEdtGrid() {
        return edtGrid;
    }

}
