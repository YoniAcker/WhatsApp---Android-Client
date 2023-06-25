package com.example.whatsapp.api;

import com.example.whatsapp.entities.Contact;
import com.example.whatsapp.entities.Message;
import com.example.whatsapp.entities.User;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {
    @GET("Chats")
    Call<List<Contact>> getChats(@Header("authorization") String token);

    @POST("Chats")
    Call<Contact> addChat(@Header("authorization") String token,
                          @Body HashMap<String, String> requestBody);

    @GET("Users/{username}")
    Call<User> getUser(@Header("authorization") String token, @Path("username") String username);

    @GET("Chats/{id}/Messages")
    Call<List<Message>> getMessages(@Header("authorization") String token, @Path("id") int id);

    @POST("Chats/{id}/Messages")
    Call<Message> addMessage(@Header("authorization") String token,
                             @Path("id") int id, @Body HashMap<String, String> requestBody);

    @POST("Users")
    Call<Void> addUser(@Body HashMap<String, String> requestBody);

    @POST("Tokens")
    Call<String> getToken(@Body HashMap<String, String> requestBody);

}
