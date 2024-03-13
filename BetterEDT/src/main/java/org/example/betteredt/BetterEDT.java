package org.example.betteredt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import static org.example.betteredt.Parseur.TestParser;

public class BetterEDT extends Application {

    private static Scene mainScene;
    private static File darkSasukeFile = new File("BetterEDT/src/main/resources/darkSasuke.css");

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("mainScreen.fxml"));
        mainScene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setTitle("Hello!");
        stage.setScene(mainScene);
        stage.show();

//        TestParser();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void goDarkMode() {
        try {
            mainScene.getStylesheets().add(darkSasukeFile.toURI().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void goLightMode() {
        mainScene.getStylesheets().remove(darkSasukeFile.toURI().toString());
    }
}