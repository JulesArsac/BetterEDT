package org.example.betteredt;

public class User {

    private int id;
    private String username;
    private boolean admin;
    private boolean darkSasuke;
    private int defaultTime;

    public User(int id, String username, boolean admin, boolean darkSasuke, int defaultTime) {
        this.id = id;
        this.username = username;
        this.admin = admin;
        this.darkSasuke = darkSasuke;
        this.defaultTime = defaultTime;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isDarkSasuke() {
        return darkSasuke;
    }

    public int getDefaultTime() {
        return defaultTime;
    }
}
