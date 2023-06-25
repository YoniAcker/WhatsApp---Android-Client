package com.example.whatsapp.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.whatsapp.entities.Message;
import com.example.whatsapp.repositories.MessagesRepository;

import java.util.List;

public class MessagesViewModel extends AndroidViewModel {
    private MessagesRepository repository;
    private LiveData<List<Message>> messages;

    public MessagesViewModel(Application application) {
        super(application);
    }

    public void setViewModel(Application application, int id, String serverURL, String token) {
        repository = new MessagesRepository(application.getApplicationContext(), id, serverURL, token);
        messages = repository.getAll();
    }

    public LiveData<List<Message>> get() {
        return messages;
    }

    public void add(String content) {
        repository.add(content);
    }
    public void reload() {
        repository.reload();
        messages = repository.getAll();
    }
}
