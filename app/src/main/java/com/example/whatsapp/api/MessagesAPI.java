package com.example.whatsapp.api;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.dao.MessagesDao;
import com.example.whatsapp.entities.Message;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MessagesAPI {
    private final MutableLiveData<List<Message>> postListData;
    private final MessagesDao dao;
    private final WebServiceAPI webServiceAPI;
    private final String token;

    public MessagesAPI(MutableLiveData<List<Message>> postListData, MessagesDao dao, String serverURL, String token) {
        this.token = token;
        this.postListData = postListData;
        this.dao = dao;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void getMessages(int id) {
        Call<List<Message>> call = webServiceAPI.getMessages(token, id);
        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(@NonNull Call<List<Message>> call, @NonNull Response<List<Message>> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.clear();
                        dao.insert(response.body().toArray(new Message[0]));
                        postListData.postValue(dao.get());
                    }).start();
                } else {
                    new Thread(dao::clear).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Message>> call, @NonNull Throwable t) {
                new Thread(dao::clear).start();
            }
        });
    }

    public void add(int id, String msg) {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("msg", msg);
        Call<Message> call = webServiceAPI.addMessage(token, id, requestBody);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(@NonNull Call<Message> call, @NonNull Response<Message> response) {
                if (response.isSuccessful()) {
                    new Thread(() -> {
                        dao.insert(response.body());
                        postListData.postValue(dao.get());
                    }).start();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Message> call, @NonNull Throwable t) {
            }
        });
    }
}