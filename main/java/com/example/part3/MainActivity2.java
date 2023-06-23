package com.example.part3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class MainActivity2 extends AppCompatActivity {
    private WebServiceAPI apiService;

    private static final int PICK_IMAGE = 100;
    private boolean is_start_user_name = false;
    private boolean is_start_password = false;
    private boolean is_start_ver = false;
    private boolean is_start_display_name = false;
    private boolean is_start_image = false;

    private ImageView user_image;
    private TextView errors;
    private EditText user_name_register;
    private Uri imageUri;

    private EditText password_register;
    private EditText ver_password_register;
    private EditText display_name_register;
    private Button btn_image_button;
    private Button register_btn_finish;
    private ActivityResultLauncher<Intent> galleryLauncher;


    private boolean is_user_name_correct = false;

    private boolean is_password_has_strings_ints = false;
    private boolean is_ver_eq_pas = false;
    private boolean is_password_has_8_chars = false;
    private boolean is_display_name_correct = false;
    private boolean is_image = false;
    private String user_name_register_content = "";

    private String password_register_content = "";
    private String ver_register_content = "";
    private String display_name_register_content = "";


    private void checkErrors() {
        String er = "";
        if (is_start_user_name && !is_user_name_correct) {
            er = er + "The username was not entered as instructed.\n";
        }
        if (is_start_image && !is_image) {
            er = er + "A photo must be uploaded.\n";
        }
        if (is_start_display_name && !is_display_name_correct) {
            er = er + "The display name was not entered as instructed.\n";
        }
        if (is_start_password) {
            if (is_start_ver && !is_ver_eq_pas) {
                er = er + "The password and password verification do not match.\n";
            }
            if (!is_password_has_strings_ints) {
                er = er + "The password must combine letters and numbers.\n";
            }
            if (!is_password_has_8_chars) {
                er = er + "The password must contain at least 8 characters\n";
            }
        }
        errors.setText(er);
    }

    private void checkErrorsBeforeRegister() {
        String er = "";
        if (!is_user_name_correct) {
            er = er + "The username was not entered as instructed.\n";
        }
        if (!is_image) {
            er = er + "A photo must be uploaded.\n";
        }
        if (!is_display_name_correct) {
            er = er + "The display name was not entered as instructed.\n";
        }

        if (!is_ver_eq_pas) {
            er = er + "The password and password verification do not match.\n";
        }
        if (!is_password_has_strings_ints) {
            er = er + "The password must combine letters and numbers.\n";
        }
        if (!is_password_has_8_chars) {
            er = er + "The password must contain at least 8 characters\n";
        }

        errors.setText(er);
        if (is_user_name_correct && is_image && is_display_name_correct && is_ver_eq_pas
                && is_password_has_strings_ints && is_password_has_8_chars) {
            sendUser();
            errors.setText("end of register\n");

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        Button alreadyRegistered = findViewById(R.id.ar_btn);
        alreadyRegistered.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        });
        user_name_register = findViewById(R.id.user_name_register);
        ver_password_register = findViewById(R.id.ver_password_register);
        btn_image_button = findViewById(R.id.btn_upload_image);
        errors = findViewById(R.id.error_of_register);
        errors = findViewById(R.id.error_of_register);
        password_register = findViewById(R.id.password_register);
        display_name_register = findViewById(R.id.register_display_name);
        register_btn_finish = findViewById(R.id.register_finish_btn);

        register_btn_finish.setOnClickListener(v -> {
            checkErrorsBeforeRegister();
        });


        display_name_register.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                is_start_display_name = true;
                display_name_register_content = display_name_register.getText().toString();
                boolean is_not_empty = !(display_name_register_content.equals(""));

                char[] charArray = display_name_register_content.toCharArray();
                boolean is_not_space = true;
                for (char c : charArray) {
                    if (c == ' ') {
                        is_not_space = false;
                        break;
                    }
                }
                is_display_name_correct = (is_not_space && is_not_empty);
                checkErrors();
            }
        });


        user_name_register.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                is_start_user_name = true;
                user_name_register_content = user_name_register.getText().toString();
                boolean is_not_empty = !(user_name_register_content.equals(""));

                char[] charArray = user_name_register_content.toCharArray();
                boolean is_not_space = true;
                for (char c : charArray) {
                    if (c == ' ') {
                        is_not_space = false;
                        break;
                    }
                }
                is_user_name_correct = (is_not_space && is_not_empty);
                checkErrors();
            }
        });


        btn_image_button.setOnClickListener(v -> {
            is_start_image = true;
            Intent gallery = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            galleryLauncher.launch(gallery);
        });


        password_register.setOnFocusChangeListener((v, hasFocus) -> {
            is_start_password = true;
            password_register_content = password_register.getText().toString();
            boolean has_char = false;
            boolean has_int = false;
            is_password_has_8_chars = password_register_content.length() > 7;
            char[] charArray = password_register_content.toCharArray();

            for (char c : charArray) {
                if (c >= '0' && c <= '9') {
                    has_int = true;
                }
                if (c >= 'a' && c <= 'z') {
                    has_char = true;
                }
                if (c >= 'A' && c <= 'Z') {
                    has_char = true;
                }
            }

            is_password_has_strings_ints = has_char && has_int;


            is_ver_eq_pas = (password_register_content.equals(ver_register_content));
            checkErrors();
        });


        ver_password_register.setOnFocusChangeListener((v, hasFocus) -> {
            ver_register_content = ver_password_register.getText().toString();
            is_start_ver = true;
            is_ver_eq_pas = (ver_register_content.equals(password_register_content));
            checkErrors();
        });


        // Initialize the ActivityResultLauncher
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            is_image = true;
                            imageUri = data.getData();
                            user_image = findViewById(R.id.user_img);
                            user_image.setImageURI(imageUri);
                        } else {
                            is_image = false;
                            checkErrors();
                        }
                    }
                });
    }


    protected int sendUser() {

//        ServerConnectionTask task = new ServerConnectionTask();
//        task.execute(new User(user_name_register_content, display_name_register_content, password_register_content, imageUri));
//        UserApi api = new UserApi("http://localhost:5000/api/");
        UserApi api = new UserApi("http://10.0.2.2:5000/api/");
//        UserApi api = new UserApi("http://192.168.56.1:5000");
        Boolean b [] = new Boolean [1];
        Boolean res ;
        api.addUser(new User(user_name_register_content, display_name_register_content, password_register_content, imageUri), b);
        Intent i;
        if (b[0]) {
            i = new Intent(this, MainActivityToDelete4.class);
        } else {
            i = new Intent(this, MainActivityToDelete3.class);
        }
        startActivity(i);
        return 0;
    }

    protected int sendUser2() {
        URL url = null;
        try {
            url = new URL("http://10.0.0.1:5000/api/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        }
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        String requestBody = "{\"username\": \"" + user_name_register_content + "\", \"password\": \"" + password_register_content + "\"}";
        String token = "";
        try {
            DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
            outputStream.writeBytes(requestBody);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            String responseCode= String.valueOf(connection.getResponseCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder responseBody;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;

            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            token = String.valueOf(new StringBuilder(response.toString())); // Assign the response to the variable
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        connection.disconnect();

        return 2;
}

}

