package com.example.whatsapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import com.example.whatsapp.entities.Message;

import java.util.List;

@Dao
public interface MessagesDao {
    @Query("SELECT * FROM message ORDER BY created DESC")
    List<Message> get();

    @Insert
    void insert(Message... messages);

    @Query("DELETE FROM message")
    void clear();
}

