package org.example.betteredt;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EventTooltipController implements Initializable {

    private EventCalendrier event;

    @FXML
    VBox root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.getChildren().clear();
        if (event != null) {
            int fontSize = 12;
            if (!Objects.equals(event.getUCE(), "NULL") && !Objects.equals(event.getUCE(), "null") && event.getUCE() != null){
                Label uce = new Label("UCE: " + event.getUCE());
                uce.setStyle("-fx-font-size: " + fontSize + "px;");
                root.getChildren().add(uce);
            }
            if (!Objects.equals(event.getProfesseur(), "NULL") && !Objects.equals(event.getProfesseur(), "null") && event.getProfesseur() != null) {
                Label professeur = new Label("Professeur: " + event.getProfesseur());
                professeur.setStyle("-fx-font-size: " + fontSize + "px;");
                root.getChildren().add(professeur);
            }
            if (!Objects.equals(event.getTypeDeCours(), "NULL") && !Objects.equals(event.getTypeDeCours(), "null") && event.getTypeDeCours() != null) {
                Label typeDeCours = new Label("Type de cours: " + event.getTypeDeCours());
                typeDeCours.setStyle("-fx-font-size: " + fontSize + "px;");
                root.getChildren().add(typeDeCours);
            }
            if (!Objects.equals(event.getLocation(), "NULL") && !Objects.equals(event.getLocation(), "null") && event.getLocation() != null) {
                Label location = new Label("Salle: " + event.getLocation());
                location.setStyle("-fx-font-size: " + fontSize + "px;");
                root.getChildren().add(location);
            }
            if (!Objects.equals(event.getElevesConcerner(), "NULL") && !Objects.equals(event.getElevesConcerner(), "null") && event.getElevesConcerner() != null) {
                Label elevesConcerner = new Label("Élèves concernés: " + event.getElevesConcerner());
                elevesConcerner.setStyle("-fx-font-size: " + fontSize + "px;");
                root.getChildren().add(elevesConcerner);
            }
            if (!Objects.equals(event.getAdditionalInfo(), "NULL") && !Objects.equals(event.getAdditionalInfo(), "null") && event.getAdditionalInfo() != null) {
                Label additionalInfo = new Label("Informations supplémentaires: " + event.getAdditionalInfo());
                additionalInfo.setStyle("-fx-font-size: " + fontSize + "px;");
                root.getChildren().add(additionalInfo);
            }
        }
    }

    public void setEvent(EventCalendrier event) {
        this.event = event;
        initialize(null, null);
    }

}
