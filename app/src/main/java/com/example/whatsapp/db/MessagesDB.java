package com.example.whatsapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.whatsapp.dao.MessagesDao;
import com.example.whatsapp.entities.Message;

@Database(entities = {Message.class}, version = 1)
public abstract class MessagesDB extends RoomDatabase {
    private static MessagesDB instance;
    public abstract MessagesDao messagesDao();
    public static synchronized MessagesDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    MessagesDB.class, "MessagesDB").build();
        }
        return instance;
    }
}
