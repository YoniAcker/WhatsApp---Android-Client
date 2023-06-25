package com.example.whatsapp.repositories;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.api.MessagesAPI;
import com.example.whatsapp.dao.MessagesDao;
import com.example.whatsapp.db.MessagesDB;
import com.example.whatsapp.entities.Message;

import java.util.LinkedList;
import java.util.List;

public class MessagesRepository {
    private final MessageListData messageListData;
    private final MessagesAPI api;
    private final int id;
    public MessagesRepository(Context context, int id, String serverURL, String token) {
        this.id = id;
        MessagesDB db = MessagesDB.getInstance(context);
        MessagesDao dao = db.messagesDao();
        messageListData = new MessageListData();
        api = new MessagesAPI(messageListData, dao, serverURL, token);
        api.getMessages(id);
    }

    static class MessageListData extends MutableLiveData<List<Message>> {
        public MessageListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    public LiveData<List<Message>> getAll() {
        return messageListData;
    }

    public void add(final String content) {
        api.add(id, content);
    }

    public void reload() {
        api.getMessages(id);
    }
}
