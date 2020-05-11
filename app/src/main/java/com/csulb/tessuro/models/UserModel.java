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
    private final String FULLNAME = "fullname";
    private final String EMAIL = "email";
    private final String ROLE = "role";
    private final String CREATED = "created";
    private final String IMGURL = "imgUrl";

    public UserModel(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void setUser(String fullname, String email, String role, String created) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(FULLNAME, fullname);
        editor.putString(EMAIL, email);
        editor.putString(ROLE, role);
        editor.putString(CREATED, created);
        editor.apply();
    }

    public void setFullname(String fullname) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(FULLNAME, this.fullname);
        editor.apply();
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(EMAIL, this.email);
        editor.apply();
    }

    public void setRole(String role) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(ROLE, this.role);
        editor.apply();
    }

    public void setImgUrl(String imgUrl) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(IMGURL, this.imgUrl);
        editor.apply();
    }

    public void setCreated(String created) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(CREATED, this.created);
        editor.apply();
    }

    public String getFullname() {
        return this.sharedPreferences.getString(FULLNAME, "");
    }

    public String getEmail() {
        return this.sharedPreferences.getString(EMAIL, "");
    }

    public String getRole() {
        return this.sharedPreferences.getString(ROLE, "");
    }

    public String getImgUrl() {
        return this.sharedPreferences.getString(IMGURL, "");
    }

    public String getCreated() {
        return this.sharedPreferences.getString(CREATED, "");
    }
}
