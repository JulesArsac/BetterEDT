package org.example.betteredt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;

public class BetterEDT extends Application {

    private static Scene mainScene;
    private static Stage stage;
    private static File darkSasukeFile = new File("src/main/resources/darkSasuke.css");

    public static Connection getConn() {
        return conn;
    }

    private static Connection conn = null;

    private static User user = null;

    @Override
    public void start(Stage stage) throws IOException {

        this.stage = stage;

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database/users.db");
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        user = new User(1, "admin", true, false, 1);
        if (user == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("connectionScreen.fxml"));
            mainScene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setTitle("BetterEDT");
            stage.setScene(mainScene);
            stage.show();
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("formationScreen.fxml"));
            mainScene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setTitle("BetterEDT");
            stage.setScene(mainScene);
            stage.show();
        }


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
        if (user != null) {
            String updateSQL = "UPDATE users SET darkSasuke = 1 WHERE id = " + user.getId() + ";";
            try {
                conn.createStatement().executeUpdate(updateSQL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void goLightMode() {
        mainScene.getStylesheets().remove(darkSasukeFile.toURI().toString());
        if (user != null) {
            String updateSQL = "UPDATE users SET darkSasuke = 0 WHERE id = " + user.getId() + ";";
            try {
                conn.createStatement().executeUpdate(updateSQL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createUser(String username, String password) throws UserNotFountException, WrongPasswordException {
        String selectSQL = "SELECT * FROM users WHERE username = '" + username + "';";
        try {
            ResultSet rs = conn.createStatement().executeQuery(selectSQL);
            if (!rs.next()) {
                throw new UserNotFountException("User not found");
            }
            else {
                String passwordDB = rs.getString("password");
                if (passwordDB.equals(password)) {
                    user = new User(rs.getInt("id"), rs.getString("username"), rs.getInt("admin") == 1, rs.getInt("darkSasuke") == 1, rs.getInt("defaultTime"));
                }
                else {
                    throw new WrongPasswordException("Wrong password");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void goToMainScreen() {
        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("formationScreen.fxml"));
                mainScene = new Scene(fxmlLoader.load(), 1000, 600);
                stage.setScene(mainScene);
                stage.show();
                formationController controller = fxmlLoader.getController();
                if (user.isDarkSasuke()) {
                    controller.setDarkMode(true);
                    goDarkMode();
                }
                else {
                    controller.setDarkMode(false);
                    goLightMode();
                }
                controller.changePrefType(user.getDefaultTime());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void goToPersonalScreen() {
        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("personalSchedule.fxml"));
                mainScene = new Scene(fxmlLoader.load(), 1000, 600);
                stage.setScene(mainScene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void switchToSalleSchedule() {
        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("salleSchedule.fxml"));
                mainScene = new Scene(fxmlLoader.load(), 1000, 600);
                stage.setScene(mainScene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static void switchToCustomEventMenu() {
        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("createEvent.fxml"));
                mainScene = new Scene(fxmlLoader.load(), 1000, 600);
                stage.setScene(mainScene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static User getUser() {
        return user;
    }

    public static void setPrefTime(int time) {
        if (user != null) {
            String updateSQL = "UPDATE users SET defaultTime = " + time + " WHERE id = " + user.getId() + ";";
            try {
                conn.createStatement().executeUpdate(updateSQL);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<EventCalendrier> parseFile(String path) {
        return Parser.startParser(path);
    }

}