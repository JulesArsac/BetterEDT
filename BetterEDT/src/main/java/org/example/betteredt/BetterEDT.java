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
import java.sql.Statement;
import java.sql.ResultSet;

import static org.example.betteredt.Parseur.TestParser;

public class BetterEDT extends Application {

    private static Scene mainScene;
    private static File darkSasukeFile = new File("BetterEDT/src/main/resources/darkSasuke.css");
    private static Connection conn = null;
    private static User user = null;

    @Override
    public void start(Stage stage) throws IOException {

//        try {
//            conn = DriverManager.getConnection("jdbc:sqlite:BetterEDT/src/main/resources/database/users.db");
//            System.out.println("Connection to SQLite has been established.");
//
////            String dropTableSQL = "DROP TABLE IF EXISTS users;";
////            conn.createStatement().execute(dropTableSQL);
////
////            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
////                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
////                    + "username TEXT NOT NULL CHECK(length(username) <= 255),"
////                    + "password TEXT NOT NULL CHECK(length(password) <= 2048),"
////                    + "admin INTEGER DEFAULT 0 CHECK(admin IN (0, 1)),"
////                    + "darkSasuke INTEGER DEFAULT 0 CHECK(darkSasuke IN (0, 1)),"
////                    + "defaultTime INTEGER DEFAULT 0"
////                    + ");";
////            conn.createStatement().execute(createTableSQL);
//
//
////            String insertSQL = "INSERT INTO users (username, password, admin, darkSasuke, defaultTime) VALUES ('admin', 'admin', 1, 1, 0);";
////            conn.createStatement().execute(insertSQL);
//
//            String selectSQL = "SELECT * FROM users";
//            ResultSet rs = conn.createStatement().executeQuery(selectSQL);
//            if (!rs.next()) {
//                System.out.println("ResultSet is empty");
//            } else {
//                do {
//                    System.out.println("id = " + rs.getInt("id"));
//                    System.out.println("username = " + rs.getString("username"));
//                    System.out.println("password = " + rs.getString("password"));
//                    System.out.println("admin = " + rs.getInt("admin"));
//                    System.out.println("darkSasuke = " + rs.getInt("darkSasuke"));
//                    System.out.println("defaultTime = " + rs.getInt("defaultTime"));
//                } while (rs.next());
//            }
//
//            user = createUser("admin", "admin");
//            System.out.println("User: " + user.getUsername() + " is admin: " + user.isAdmin() + " darkSasuke: " + user.isDarkSasuke() + " defaultTime: " + user.getDefaultTime());
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (UserNotFountException e) {
//            throw new RuntimeException(e);
//        } catch (WrongPasswordException e) {
//            throw new RuntimeException(e);
//        }


        if (user == null) {
            FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("connectionScreen.fxml"));
            mainScene = new Scene(fxmlLoader.load(), 1000, 600);
            stage.setTitle("BetterEDT");
            stage.setScene(mainScene);
            stage.show();
        }
        else {
            FXMLLoader fxmlLoader = new FXMLLoader(BetterEDT.class.getResource("mainScreen.fxml"));
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
    }

    public static void goLightMode() {
        mainScene.getStylesheets().remove(darkSasukeFile.toURI().toString());
    }

    public static User createUser(String username, String password) throws UserNotFountException, WrongPasswordException {
        String selectSQL = "SELECT * FROM users WHERE username = '" + username + "';";
        try {
            ResultSet rs = conn.createStatement().executeQuery(selectSQL);
            if (!rs.next()) {
                throw new UserNotFountException("User not found");
            }
            else {
                String passwordDB = rs.getString("password");
                if (passwordDB.equals(password)) {
                    return new User(rs.getInt("id"), rs.getString("username"), rs.getInt("admin") == 1, rs.getInt("darkSasuke") == 1, rs.getInt("defaultTime"));
                }
                else {
                    throw new WrongPasswordException("Wrong password");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}