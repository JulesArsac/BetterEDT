package org.example.betteredt;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class weeklyGridController implements Initializable {


    List<List<EventCalendrier>> weeklyList;
    @FXML
    AnchorPane rootPane;
    @FXML
    GridPane edtGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VBox vbox = new VBox();
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            edtGrid.setPrefWidth(newVal.doubleValue());
            edtGrid.setMaxWidth(newVal.doubleValue());
//            vbox.setPrefWidth(newVal.doubleValue()/5);
//            System.out.println(vbox.getPrefWidth());
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


        vbox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        vbox.setMaxHeight(Double.MAX_VALUE);
        vbox.setSpacing(0);

        vbox.prefWidthProperty().bind(edtGrid.widthProperty().divide(5));
        vbox.prefHeightProperty().bind(edtGrid.heightProperty().subtract(row1.prefHeightProperty()));

        //TODO: Marche pas psq je vais avoir un trou une colonne sur 2 si ya qu'un seul cours envie de crever

        for (int i = 0; i < 23; i++) {
            if (i == 5 || i == 12 || i == 22) {
                Rectangle blueRectangle = new Rectangle();
                blueRectangle.widthProperty().bind(vbox.prefWidthProperty());
                blueRectangle.heightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
                blueRectangle.setFill(Color.BLUE);
                vbox.getChildren().add(blueRectangle);
                System.out.println("Added blue rectangle");
            } else {
                Region spacer = new Region();
                spacer.prefHeightProperty().bind(Bindings.divide(vbox.heightProperty(), 23));
                vbox.getChildren().add(spacer);
            }
        }
        edtGrid.add(vbox, 0, 1);

    }

    public void setWeeklyList(List<List<EventCalendrier>> weeklyList) {
        this.weeklyList = weeklyList;
        initialize(null, null);
    }

}
