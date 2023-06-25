package com.example.whatsapp.entities;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


@Entity
public class Contact {
    @PrimaryKey
    @SerializedName("id")
    private final int contactID;
    @Embedded
    private final User user;
    @Embedded
    private Message lastMessage;

    public Contact(int contactID, User user) {
        this.contactID = contactID;
        this.user = user;
        lastMessage = null;
    }

    public int getContactID() {
        return contactID;
    }

    public User getUser() {
        return user;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }
}
