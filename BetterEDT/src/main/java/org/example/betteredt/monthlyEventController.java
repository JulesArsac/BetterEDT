package org.example.betteredt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class monthlyEventController {

    @FXML
    Label dayNum;
    @FXML
    Label eventNum;
    @FXML
    VBox root;

    public void setDayNum(String dayNum) {
        this.dayNum.setText(dayNum);
    }

    public void setEventNum(String eventNum) {
        this.eventNum.setText(eventNum);
    }

    public void setEval(boolean eval) {
        if (eval) {
            root.setStyle("-fx-background-color: #f56e64; -fx-border-color: black; -fx-border-width: 1px;");
        }
        else {
            root.setStyle("-fx-background-color: #9dc1fc; -fx-border-color: black; -fx-border-width: 1px;");
        }
    }

    public void setEmpty() {
        root.setStyle("-fx-background-color: #b0b0b0; -fx-border-color: black; -fx-border-width: 1px;");
    }

}
