package com.example.whatsapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.whatsapp.dao.ContactsDao;
import com.example.whatsapp.entities.Contact;
import com.example.whatsapp.entities.Message;
import com.example.whatsapp.entities.User;

@Database(entities = {Contact.class, User.class, Message.class}, version = 1)
public abstract class ContactsDB extends RoomDatabase {
    private static ContactsDB instance;
    public abstract ContactsDao contactsDao();
    public static synchronized ContactsDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ContactsDB.class, "ContactsDB").build();
        }
        return instance;
    }
}
