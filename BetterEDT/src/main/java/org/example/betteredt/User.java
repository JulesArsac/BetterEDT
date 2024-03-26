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

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setDarkSasuke(boolean darkSasuke) {
        this.darkSasuke = darkSasuke;
    }

    public void setDefaultTime(int defaultTime) {
        this.defaultTime = defaultTime;
    }
}
