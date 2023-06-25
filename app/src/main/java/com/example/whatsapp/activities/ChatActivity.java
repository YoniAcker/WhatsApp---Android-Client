package com.example.whatsapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.whatsapp.R;
import com.example.whatsapp.adapters.MessageListAdapter;
import com.example.whatsapp.entities.Message;
import com.example.whatsapp.entities.Sender;
import com.example.whatsapp.entities.User;
import com.example.whatsapp.viewmodels.MessagesViewModel;


public class ChatActivity extends AppCompatActivity {
    private MessagesViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        User user = (User) getIntent().getSerializableExtra("user");
        TextView userName = findViewById(R.id.nameChat);
        userName.setText(user.getDisplayName());
        ImageView userPic = findViewById(R.id.picChat);
        String imageData = user.getProfilePic().substring(21);
        byte[] decodedBytes = Base64.decode(imageData, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        userPic.setImageBitmap(decodedBitmap);
        viewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        viewModel.setViewModel(getApplication(), getIntent().getIntExtra("id", 0),
                getIntent().getStringExtra("serverURL"),
                getIntent().getStringExtra("token"));
        RecyclerView lstMessages = findViewById(R.id.lstMessage);
        MessageListAdapter adapter = new MessageListAdapter(this, user.getUsername());
        lstMessages.setAdapter(adapter);
        lstMessages.setLayoutManager(new LinearLayoutManager(this));
        SwipeRefreshLayout refreshLayout = findViewById(R.id.chatRefreshLayout);
        refreshLayout.setOnRefreshListener(() -> viewModel.reload());
        viewModel.get().observe(this, messageList -> {
            adapter.SetMessages(messageList);
            refreshLayout.setRefreshing(false);
        });
        EditText msgText = findViewById(R.id.textMessage);
        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(view -> {
            viewModel.add(msgText.getText().toString());
            msgText.setText("");
        });
        Intent resultIntent = new Intent();
        resultIntent.putExtra("id", getIntent().getIntExtra("id", 0));
        setResult(Activity.RESULT_OK, resultIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent resultIntent = new Intent();
        if (viewModel.get().getValue().isEmpty()) {
            resultIntent.putExtra("message", new Message(0, new Sender(""), "", ""));
        } else {
            Message message = viewModel.get().getValue().get(0);
            resultIntent.putExtra("message", message);
        }
        resultIntent.putExtra("id", getIntent().getIntExtra("id", 0));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}