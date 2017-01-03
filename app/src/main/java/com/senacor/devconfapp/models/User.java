package com.senacor.devconfapp.models;

/**
 * Created by Marynasuprun on 07.11.2016.
 */
public class User {


    private String username;
    private String password;
    private String userId;


    public User() {

    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}

