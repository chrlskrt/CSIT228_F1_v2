package com.example.csit228_f1_v2;

public class CurrentUser {
    private static CurrentUser instance;
    private int user_id;
    private String username;
    private String password;

    public static CurrentUser getInstance(){
        if (instance == null){
            instance = new CurrentUser();
        }

        return instance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
