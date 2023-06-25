package com.example.whatsapp.repositories;


import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.api.ContactsAPI;
import com.example.whatsapp.dao.ContactsDao;
import com.example.whatsapp.db.ContactsDB;
import com.example.whatsapp.entities.Contact;
import com.example.whatsapp.entities.Message;
import com.example.whatsapp.entities.User;

import java.util.LinkedList;
import java.util.List;

public class ContactsRepository {
    private final ContactsDao dao;
    private final ContactListData contactListData;
    private ContactsAPI api;
    private String serverURL;
    private final String token;

    public ContactsRepository(Context context, String serverURL, String token) {
        this.token = token;
        this.serverURL = serverURL;
        ContactsDB db = ContactsDB.getInstance(context);
        dao = db.contactsDao();
        contactListData = new ContactListData();
        api = new ContactsAPI(contactListData, dao, serverURL, token);
        api.getContacts();
    }

    static class ContactListData extends MutableLiveData<List<Contact>> {
        public ContactListData() {
            super();
            setValue(new LinkedList<>());
        }
    }

    public LiveData<List<Contact>> getAll() {
        return contactListData;
    }

    public void add(final String name) {
        api.add(name);
    }
    public User getUser(String username) {
        User user = new User("", "", "");
        api.getUser(user, username);
        return user;
    }

    public void reload() {
        api.getContacts();
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
        api = new ContactsAPI(contactListData, dao, serverURL, token);
    }

    public String getServerURL() {
        return serverURL;
    }
    public void setLastMessage(int id, Message message) {
        new Thread(() -> {
            Contact contact = dao.get(id);
            contact.setLastMessage(message);
            dao.update(contact);
            contactListData.postValue(dao.get());
        }).start();
    }
}
