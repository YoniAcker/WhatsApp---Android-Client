package com.example.whatsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import com.example.whatsapp.R;

public class Settings2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        Button btnSubmit = findViewById(R.id.btnSubmit);
        EditText serverAddress = findViewById(R.id.serverAddress);
        serverAddress.setText(getIntent().getStringExtra("serverURL"));
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
    }
}