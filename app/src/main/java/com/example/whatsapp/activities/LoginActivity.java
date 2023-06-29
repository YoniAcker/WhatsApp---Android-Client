package com.example.whatsapp.activities;

import com.google.firebase.messaging.FirebaseMessaging;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.whatsapp.R;
import com.example.whatsapp.viewmodels.ErrorViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {
    String serverURL = "http://10.0.2.2:5000/api/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnRegister = findViewById(R.id.register_btn);
        btnRegister.setOnClickListener(v -> {
            Intent i = new Intent(this, RegisterActivity.class);
            i.putExtra("serverURL", serverURL);
            startActivity(i);
        });

        FirebaseApp.initializeApp(this);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, instanceIdResult -> {
            String newToken = instanceIdResult.getToken();

        });


        ErrorViewModel viewModel = new ViewModelProvider(this).get(ErrorViewModel.class);
        Button btnLogin = findViewById(R.id.login_btn);
        EditText usernameEdit = findViewById(R.id.user_name_login), passwordEdit = findViewById(R.id.password_login);
        TextView errors = findViewById(R.id.error_of_login);
        btnLogin.setOnClickListener(view -> {
            String username = usernameEdit.getText().toString(), password = passwordEdit.getText().toString();
            if (username.equals("")) {
                errors.setText("Please enter username.");
            } else if (password.equals("")) {
                errors.setText("Please enter password.");
            } else {
                viewModel.login(username, password, serverURL);
            }
        });
        viewModel.get().observe(this, error -> {
            if (error.equals("Incorrect username and/or password") || error.equals("")) {
                errors.setText(error);
            } else {
                Intent intent = new Intent(this, ContactsActivity.class);
                intent.putExtra("token", "Bearer: " + error);
                intent.putExtra("username", usernameEdit.getText().toString());
                intent.putExtra("serverURL", serverURL);
                startActivity(intent);
            }
        });
        FloatingActionButton btnSettings = findViewById(R.id.loginbtnSettings);
        ActivityResultLauncher<Intent> getServerURL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            serverURL = data.getStringExtra("serverURL");
                        }
                    }
                }
        );
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, Settings2Activity.class);
            intent.putExtra("serverURL", serverURL);
            getServerURL.launch(intent);
        });
    }

}