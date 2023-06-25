package com.example.whatsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.whatsapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        EditText serverAddress = findViewById(R.id.serverAddress);
        serverAddress.setText(getIntent().getStringExtra("serverURL"));
        if (!getIntent().getStringExtra("profilePic").equals("")) {
            TextView userName = findViewById(R.id.nameUser);
            userName.setText(getIntent().getStringExtra("displayName"));
            ImageView userPic = findViewById(R.id.picUser);
            String imageData = getIntent().getStringExtra("profilePic").substring(21);
            byte[] decodedBytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            userPic.setImageBitmap(decodedBitmap);
        }
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch nightMode = findViewById(R.id.switchNightMode);
        btnSubmit.setOnClickListener(view -> {
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES && !nightMode.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO && nightMode.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("serverURL", serverAddress.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
        Button btnLogout = findViewById(R.id.logoutButton);
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }
}