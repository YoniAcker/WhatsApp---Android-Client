package com.example.whatsapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.whatsapp.api.UserAPI;
import com.example.whatsapp.entities.User;

public class ErrorViewModel extends ViewModel {
    private final MutableLiveData<String> error;
    public ErrorViewModel() {
        super();
        error = new MutableLiveData<>();
        error.setValue("");
    }
    public MutableLiveData<String> get() {
        return error;
    }
    public void login(String username, String password, String serverURL) {
        UserAPI userAPI = new UserAPI(serverURL, error);
        userAPI.loginUser(username, password);
    }
    public void register(User user, String password, String serverURL) {
        UserAPI userAPI = new UserAPI(serverURL, error);
        userAPI.addUser(user, password);
    }
}
