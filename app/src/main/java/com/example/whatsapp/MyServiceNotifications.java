package com.example.whatsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyServiceNotifications extends FirebaseMessagingService {
    public MyServiceNotifications() {
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

    }


}