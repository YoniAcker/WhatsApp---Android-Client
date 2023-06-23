package com.example.part3;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserApi {
    private MutableLiveData<List<User>> postListData;
    //private UsersDao dao;
    Retrofit retrofit;
    WebServiceAPI webServiceAPI;


//    public UserApi(MutableLiveData<List<User>> postListData, UsersDao dao, String serverURL) {
//        this.postListData = postListData;
//        //  this.dao = dao;
//        retrofit = new Retrofit.Builder()
//                .baseUrl(serverURL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        webServiceAPI = retrofit.create(WebServiceAPI.class);
//    }

    public UserApi(String serverURL) {
        retrofit = new Retrofit.Builder()
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webServiceAPI = retrofit.create(WebServiceAPI.class);
    }

    public String loginUser(String username, String password) {

        final String[] man = new String[1];
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("password", password);


        Call<String> call = webServiceAPI.getToken(requestBody);

        call.enqueue(new Callback<String>() {
                         @Override
                         public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                             if (response.code() == 200) {
                                 man[0] = response.body();
                             } else {
                                 man[0] = "error";
                             }
                         }

                         @Override
                         public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

                         }
                     }
        );
        return man[0];
    }

    public void addUser(User user, Boolean [] man) {
//        public boolean addUser(User user) {

//        CountDownLatch cdl = new CountDownLatch(1);

//        final boolean[] man = new boolean[1];
        final String[] res = new String[1];
        res[0] = "start of: \n";
        HashMap<String, String> requestBody = new HashMap<>();
        requestBody.put("username", user.getUser_name());
        requestBody.put("password", user.getPassword());
        requestBody.put("displayName", user.getDisplay_name());
        requestBody.put("profilePic", user.getImg().toString());
        Call<User> call = webServiceAPI.addUser(requestBody);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                man[0 ] = true;
            }
            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                man[0] = true;
            }
        });
//        Call<Void> call = webServiceAPI.addUser(requestBody);
//
//        call.enqueue(new Callback<Void>() {
//                         @Override
//                         public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
//                             man[0] = true;
////                             cdl.countDown();
//
////                             res[0] = "res:";
//                         }
//
//                         @Override
//                         public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
////                             if (t instanceof ConnectException){
////                                 res[0] = res[0] + "connect ex\n";
////
////                             }
//                             man[0] = true;
////                             res[0] = res[0] + t.getCause().toString();
////                             res[0] = res[0] + t.getMessage().toString();
////                             res[0] = res[0] + t.getLocalizedMessage().toString();
////                             res[0] = res[0] + t.getCause().toString();
////                             res[0] = res[0] + "end of the t\n";
//
////                             cdl.countDown();
//
//
//
//                         }
//                     }
//        );
//        res[0] = "aa";
//        return man[0];

//        try {
//            cdl.await();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        return ;
    }


}