package com.example.part3;

import android.net.Uri;

public class User {

    private String user_name;
    private String display_name;
    private String password;
    private Uri img;

    public User(String user_name, String display_name, String password, Uri img) {
        this.user_name = user_name;
        this.display_name = display_name;
        this.password = password;
        this.img = img;
    }
    public String getUser_name() {
        return user_name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public String getPassword() {
        return password;
    }

    public Uri getImg() {
        return img;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImg(Uri img) {
        this.img = img;
    }

}
