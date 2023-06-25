package com.example.whatsapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.whatsapp.R;

public class AddContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Button btnAdd = findViewById(R.id.btnAdd);
        TextView etContent = findViewById(R.id.etContent);
        btnAdd.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("contact", etContent.getText().toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}