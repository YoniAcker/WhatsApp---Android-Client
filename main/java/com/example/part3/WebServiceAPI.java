package com.example.part3;


import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceAPI {


    @POST("Users")
    Call<User> addUser(@Body HashMap<String, String> requestBody);


    @POST("Tokens")
    Call<String> getToken(@Body HashMap<String, String> requestBody);

//    @GET("/api/Users/{username}")
//    Call<User> getUser(@Path("username") String username);
}