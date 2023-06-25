package com.example.whatsapp.viewmodels;



import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.whatsapp.entities.Contact;
import com.example.whatsapp.entities.Message;
import com.example.whatsapp.entities.User;
import com.example.whatsapp.repositories.ContactsRepository;

import java.util.List;

public class ContactsViewModel extends AndroidViewModel {
    private ContactsRepository repository;
    private LiveData<List<Contact>> contacts;
    private User userInfo = null;

    public ContactsViewModel(Application application) {
        super(application);
    }

    public void setViewModel(Application application, String serverURL, String token) {
        repository = new ContactsRepository(application.getApplicationContext(), serverURL, token);
        contacts = repository.getAll();
    }

    public LiveData<List<Contact>> get() {
        return contacts;
    }

    public void add(String name) {
        repository.add(name);
    }
    public void reload() {
        repository.reload();
        contacts = repository.getAll();
    }

    public String getServerURL() {
        return repository.getServerURL();
    }

    public void setServerURL(String serverURL) {
        repository.setServerURL(serverURL);
    }

    public User getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String username) {
        this.userInfo = repository.getUser(username);
    }
    public void setLastMassage (int id, Message message) {
        repository.setLastMessage(id, message);
    }
}
