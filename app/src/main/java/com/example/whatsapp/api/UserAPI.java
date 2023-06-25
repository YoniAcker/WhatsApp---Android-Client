package com.example.whatsapp.api;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.whatsapp.entities.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAPI {
    MutableLiveData<String> error;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;

    public UserAPI(String serverURL, MutableLiveData<String> error) {
        this.error = error;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public void loginUser(String username, String password) {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);
        Call<String> call = webServiceAPI.getToken(requestBody);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful()) {
                    error.postValue(response.body());
                } else {
                    error.postValue("Incorrect username and/or password");
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                error.postValue("Incorrect username and/or password");
            }
        });
    }


    public void addUser(User user, String password) {
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", user.getUsername());
        requestBody.put("password", password);
        requestBody.put("displayName", user.getDisplayName());
        requestBody.put("profilePic", user.getProfilePic());
        Call<Void> call = webServiceAPI.addUser(requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if(response.isSuccessful()) {
                    error.postValue("V");
                }
                else {
                    error.postValue("The username is already in use.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                error.postValue("The username is already in use.");
            }
        });
    }
}
