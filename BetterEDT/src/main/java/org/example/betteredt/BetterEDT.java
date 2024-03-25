package org.example.betteredt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
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

    private static Scene mainScene = null;
    private static Stage stage;
    private static File darkSasukeFile = new File("src/main/resources/darkSasuke.css");
    public static Connection getConn() {
        return conn;
    }
    private static Connection conn = null;
    private static User user = null;
    private static boolean darkMode = false;

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
        darkMode = true;
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
        darkMode = false;
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

    public static void goToFormationScreen() {
        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("formationScreen.fxml"));
                if (mainScene == null){
                    mainScene = new Scene(fxmlLoader.load(), 1000, 600);
                }
                else {
                    mainScene.setRoot(fxmlLoader.load());
                }
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
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("personalScreen.fxml"));
                mainScene.setRoot(fxmlLoader.load());
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
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("salleScreen.fxml"));
                mainScene.setRoot(fxmlLoader.load());
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
                mainScene.setRoot(fxmlLoader.load());
                stage.setScene(mainScene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void switchToReservationEventMenu(String salle) {
        if (user != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("createReservationEvent.fxml"));
                mainScene.setRoot(fxmlLoader.load());
                createEventController controller = fxmlLoader.getController();
                controller.setSalleNameField(salle);
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

    public static String getIcsName(int type) {
        String folder = switch (type) {
            case 0 -> "formation";
            case 1 -> "salle";
            case 2 -> "personnel";
            default -> "";
        };
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisissez votre fichier");
        File initialDirectory = new File("src/main/resources/" + folder);
        fileChooser.setInitialDirectory(initialDirectory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier ICS", "*.ics"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {

            if (type == 2) {
                if (user != null) {
                    String updateSQL = "UPDATE users SET defaultFile = '" + selectedFile.getName() + "' WHERE id = " + user.getId() + ";";
                    try {
                        conn.createStatement().executeUpdate(updateSQL);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static List<EventCalendrier> addUserEvent(List<EventCalendrier> events) {
        if (user == null) {
            return events;
        }
        String selectSQL = "SELECT * FROM personalSchedule WHERE user = '" + user.getUsername() + "';";
        try {
            ResultSet rs = conn.createStatement().executeQuery(selectSQL);
            while (rs.next()) {
                EventCalendrier eventToAdd = new EventCalendrier(rs.getString("description"), rs.getString("startHeure"), rs.getString("endHeure"), rs.getString("lieu"), rs.getInt("mois"), rs.getInt("jour"), rs.getInt("anner"), rs.getString("eventName"), rs.getString("user"), rs.getString("couleur"), rs.getString("description"), rs.getString("description"));
                events.add(eventToAdd);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        selectSQL = "SELECT * FROM reservationSalleTable WHERE user = '" + user.getUsername() + "';";
        try {
            ResultSet rs = conn.createStatement().executeQuery(selectSQL);
            while (rs.next()) {
                EventCalendrier eventToAdd = new EventCalendrier(rs.getString("description"), rs.getString("startHeure"), rs.getString("endHeure"), rs.getString("salleName"), rs.getInt("mois"), rs.getInt("jour"), rs.getInt("anner"), "Réservation de salle", rs.getString("user"), null, rs.getString("description"), rs.getString("description"));
                events.add(eventToAdd);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    public static List<EventCalendrier> addSalleEvent(List<EventCalendrier> events, String salle) {
        String selectSQL = "SELECT * FROM reservationSalleTable WHERE salleName = '" + salle + "';";
        try {
            ResultSet rs = conn.createStatement().executeQuery(selectSQL);
            while (rs.next()) {
                EventCalendrier eventToAdd = new EventCalendrier(rs.getString("description"), rs.getString("startHeure"), rs.getString("endHeure"), rs.getString("salleName"), rs.getInt("mois"), rs.getInt("jour"), rs.getInt("anner"), "Réservation de salle", rs.getString("user"), null, rs.getString("description"), rs.getString("description"));
                events.add(eventToAdd);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return events;

    }

}