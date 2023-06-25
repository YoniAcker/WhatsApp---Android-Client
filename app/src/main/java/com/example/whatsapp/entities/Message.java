package com.example.whatsapp.entities;


import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Message implements Serializable {
    @PrimaryKey
    @SerializedName("id")
    private final int messageID;
    @Embedded
    private final Sender sender;
    private final String content;
    private final String created;

    public Message(int messageID, Sender sender, String content, String created) {
        this.messageID = messageID;
        this.sender = sender;
        this.content = content;
        this.created = created;
    }

    public String getCreated() {
        return created;
    }

    public int getMessageID() {
        return messageID;
    }

    public Sender getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
