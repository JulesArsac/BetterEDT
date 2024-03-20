package org.example.betteredt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class weeklyGridController implements Initializable {

    @FXML
    AnchorPane rootPane;
    @FXML
    GridPane edtGrid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rootPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            edtGrid.setPrefWidth(newVal.doubleValue());
            edtGrid.setMaxWidth(newVal.doubleValue());
        });
        rootPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            edtGrid.setPrefHeight(newVal.doubleValue());
            edtGrid.setMaxHeight(newVal.doubleValue());
        });

        for (int i= 0; i < 5; i++) {
            Label label = new Label(i + "");
            edtGrid.add(label, i, 0);
        }
    }
}
