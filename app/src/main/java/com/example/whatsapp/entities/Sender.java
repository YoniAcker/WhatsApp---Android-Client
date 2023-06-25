package com.example.whatsapp.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Sender implements Serializable {
    @SerializedName("username")
    private final String name;
    public Sender (String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
