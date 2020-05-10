package com.csulb.tessuro.models;

public class UserModel {
    private String fullname;
    private String email;
    private String role;

    public UserModel(String fullname, String email, String role) {
        this.fullname = fullname;
        this.email = fullname;
        this.role = role;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
