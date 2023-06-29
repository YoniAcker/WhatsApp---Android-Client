package com.example.whatsapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.whatsapp.Interfaces.OnItemClickListener;
import com.example.whatsapp.R;
import com.example.whatsapp.adapters.ContactListAdapter;

import com.example.whatsapp.entities.Message;
import com.example.whatsapp.entities.User;
import com.example.whatsapp.viewmodels.ContactsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class ContactsActivity extends AppCompatActivity implements OnItemClickListener {

    private ContactsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        viewModel = new ViewModelProvider(this).get(ContactsViewModel.class);
        if (viewModel.getUserInfo() == null) {
            viewModel.setViewModel(getApplication(), getIntent().getStringExtra("serverURL"), getIntent().getStringExtra("token"));
            viewModel.setUserInfo(getIntent().getStringExtra("username"));
        }
        RecyclerView lstContact = findViewById(R.id.lstContact);
        ContactListAdapter adapter = new ContactListAdapter(this, this);
        lstContact.setAdapter(adapter);
        lstContact.setLayoutManager(new LinearLayoutManager(this));
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(() -> viewModel.reload());
        viewModel.get().observe(this, contactList -> {
            adapter.SetContacts(contactList);
            refreshLayout.setRefreshing(false);
        });






        FloatingActionButton btnAdd = findViewById(R.id.btnAdd);
        ActivityResultLauncher<Intent> getContact = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String name = data.getStringExtra("contact");
                            viewModel.add(name);
                        }
                    }
                }
        );
        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddContactActivity.class);
            getContact.launch(intent);
        });
        FloatingActionButton btnSettings = findViewById(R.id.btnSettings);
        ActivityResultLauncher<Intent> getServerURL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            viewModel.setServerURL(data.getStringExtra("serverURL"));
                        }
                    }
                }
        );
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.putExtra("serverURL", viewModel.getServerURL());
            intent.putExtra("displayName", viewModel.getUserInfo().getDisplayName());
            intent.putExtra("profilePic", viewModel.getUserInfo().getProfilePic());
            getServerURL.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> getLastMessage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        int id = data.getIntExtra("id", 0);
                        Message message = (Message) data.getSerializableExtra("message");
                        viewModel.setLastMassage(id, message);
                    }
                }
            }
    );
    @Override
    public void onItemClick(User user, int id) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("id", id);
        intent.putExtra("serverURL", getIntent().getStringExtra("serverURL"));
        intent.putExtra("token", getIntent().getStringExtra("token"));
        getLastMessage.launch(intent);
    }
}