package com.example.whatsapp.api;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.dao.ContactsDao;
import com.example.whatsapp.entities.Contact;
import com.example.whatsapp.entities.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ContactsAPI {
    private final MutableLiveData<List<Contact>> postListData;
    private final ContactsDao dao;
    private final String token;
    private final WebServiceAPI webServiceAPI;

    public ContactsAPI(MutableLiveData<List<Contact>> postListData, ContactsDao dao, String serverURL, String token) {
        this.token = token;
        this.postListData = postListData;
        this.dao = dao;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getContacts() {
        Call<List<Contact>> call = webServiceAPI.getChats(token);
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(@NonNull Call<List<Contact>> call, @NonNull Response<List<Contact>> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.clear();
                        dao.insert(response.body().toArray(new Contact[0]));
                        postListData.postValue(dao.get());
                    }).start();
                } else {
                    new Thread(dao::clear).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Contact>> call, @NonNull Throwable t) {
                new Thread(dao::clear).start();
            }
        });
    }

    public void add(String name) {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", name);
        Call<Contact> call = webServiceAPI.addChat(token, requestBody);
        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(@NonNull Call<Contact> call, @NonNull Response<Contact> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.insert(response.body());
                        postListData.postValue(dao.get());
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Contact> call, @NonNull Throwable t) {
            }
        });
    }

    public void getUser(User user, String username) {
        Call<User> call = webServiceAPI.getUser(token, username);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        user.setUsername(response.body().getUsername());
                        user.setDisplayName(response.body().getDisplayName());
                        user.setProfilePic(response.body().getProfilePic());
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
            }
        });

    }

}