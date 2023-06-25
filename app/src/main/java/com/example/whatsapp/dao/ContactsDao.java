package com.example.whatsapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.whatsapp.entities.Contact;

import java.util.List;

@Dao
public interface ContactsDao {
    @Query("SELECT * FROM contact ORDER BY created DESC")
    List<Contact> get();

    @Query("SELECT * FROM contact WHERE contactID = :id")
    Contact get(int id);

    @Insert
    void insert(Contact... contacts);

    @Update
    void update(Contact... contacts);
    @Query("DELETE FROM contact")
    void clear();
}
