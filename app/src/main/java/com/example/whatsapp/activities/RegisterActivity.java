package com.example.whatsapp.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.whatsapp.R;
import com.example.whatsapp.entities.User;
import com.example.whatsapp.viewmodels.ErrorViewModel;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class RegisterActivity extends AppCompatActivity {

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
    private ErrorViewModel viewModel;

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
            ContentResolver contentResolver = getContentResolver();
            viewModel.register(new User(user_name_register_content, display_name_register_content,
                    convertImageUriToBase64(imageUri, contentResolver)),
                    password_register_content, getIntent().getStringExtra("serverURL"));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button alreadyRegistered = findViewById(R.id.ar_btn);
        alreadyRegistered.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        });
        user_name_register = findViewById(R.id.user_name_register);
        ver_password_register = findViewById(R.id.ver_password_register);
        Button btn_image_button = findViewById(R.id.btn_upload_image);
        errors = findViewById(R.id.error_of_register);
        errors = findViewById(R.id.error_of_register);
        password_register = findViewById(R.id.password_register);
        display_name_register = findViewById(R.id.register_display_name);
        Button register_btn_finish = findViewById(R.id.register_finish_btn);
        viewModel = new ViewModelProvider(this).get(ErrorViewModel.class);
        viewModel.get().observe(this, error -> {
            if (error.equals("V")) {
                finish();
            } else {
                errors.setText(error);
            }
        });
        register_btn_finish.setOnClickListener(v -> checkErrorsBeforeRegister());
        display_name_register.setOnFocusChangeListener((v, hasFocus) -> {
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
        });


        user_name_register.setOnFocusChangeListener((v, hasFocus) -> {
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

    public String convertImageUriToBase64(Uri imageUri, ContentResolver contentResolver) {
        try {
            InputStream inputStream = contentResolver.openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}

