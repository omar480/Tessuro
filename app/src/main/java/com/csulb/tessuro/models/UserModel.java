package com.csulb.tessuro.models;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class UserModel {
    private String fullname;
    private String email;
    private String role;
    private String imgUrl;
    private String created;

    private SharedPreferences sharedPreferences;

    public UserModel(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setUser(String fullname, String email, String role, String created) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("fullname", fullname);
        editor.putString("email", email);
        editor.putString("role", role);
        editor.putString("created", created);
        editor.apply();
    }

    public void setFullname(String fullname) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("fullname", this.fullname);
        editor.apply();
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("email", this.email);
        editor.apply();
    }

    public void setRole(String role) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("role", this.role);
        editor.apply();
    }

    public void setImgUrl(String imgUrl) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("imgUrl", this.imgUrl);
        editor.apply();
    }

    public void setCreated(String created) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString("created", this.created);
        editor.apply();
    }

    public String getFullname() {
        System.out.println("called");
        return this.sharedPreferences.getString("fullname", "");
    }

    public String getEmail() {
        return this.sharedPreferences.getString("email", "");
    }

    public String getRole() {
        return this.sharedPreferences.getString("role", "");
    }

    public String getImgUrl() {
        return this.sharedPreferences.getString("imgUrl", "");
    }

    public String getCreated() {
        return this.sharedPreferences.getString("created", "");
    }
}
