package com.csulb.tessuro.models;

public class UserModel {
    private String username;
    private String email;
    private String role;

    public UserModel(String username, String email, String role) {
        this.username = username;
        this.email = username;
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
