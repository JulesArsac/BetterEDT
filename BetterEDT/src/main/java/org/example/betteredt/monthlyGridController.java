package org.example.betteredt;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.time.DayOfWeek;
import java.util.List;
import java.util.ResourceBundle;

public class monthlyGridController implements Initializable {

    private DayOfWeek firstDay = null;
    private List<monthEvent> eventNums = null;

    @FXML
    GridPane edtGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        edtGrid.setGridLinesVisible(true);
        if (firstDay == null || eventNums == null) {
            return;
        }
        edtGrid.getChildren().clear();
        int column = firstDay.getValue()-1;
        int row = 0;
        int dayNum = 1;
        System.out.println(eventNums.size());
        for (monthEvent event : eventNums) {
            if (column == 7) {
                column = 0;
                row++;
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("monthlyEvent.fxml"));
            Parent rootNode = null;
            try {
                rootNode = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            VBox root = (VBox) rootNode;
            root.prefHeightProperty().bind(edtGrid.heightProperty().divide(5));
            root.prefWidthProperty().bind(edtGrid.widthProperty().divide(7));

            monthlyEventController controller = fxmlLoader.getController();
            String dayName = "";
            if (column == 0) {
                dayName = "Lundi";
            }
            else if (column == 1) {
                dayName = "Mardi";
            }
            else if (column == 2) {
                dayName = "Mercredi";
            }
            else if (column == 3) {
                dayName = "Jeudi";
            }
            else if (column == 4) {
                dayName = "Vendredi";
            }
            else if (column == 5) {
                dayName = "Samedi";
            }
            else if (column == 6) {
                dayName = "Dimanche";
            }
            controller.setDayNum(dayName + " " + dayNum);
            controller.setEventNum("Nombre d'évenements: " + event.getNbEvent());
            controller.setEval(event.isEval());

            edtGrid.add(root, column, row);
            dayNum++;
            column++;
        }
    }

    public void setFirstDay(DayOfWeek firstDay) {
        this.firstDay = firstDay;
        if (eventNums != null) {
            initialize(null, null);
        }
    }

    public void setEventNums(List<monthEvent> eventNums) {
        this.eventNums = eventNums;
        if (firstDay != null) {
            initialize(null, null);
        }
    }
}
