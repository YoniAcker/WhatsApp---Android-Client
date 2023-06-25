package com.example.whatsapp.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.Interfaces.OnItemClickListener;
import com.example.whatsapp.R;
import com.example.whatsapp.entities.Contact;
import com.example.whatsapp.entities.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactViewHolder> {
    private List<Contact> contacts;
    private final LayoutInflater mInflater;
    private final OnItemClickListener listener;

    class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView picContact;
        private final TextView nameContact;
        private final TextView messageContact;
        private final TextView timeContact;
        private User user;
        private int id;

        private ContactViewHolder(View itemView) {
            super(itemView);
            picContact = itemView.findViewById(R.id.picContact);
            nameContact = itemView.findViewById(R.id.nameContact);
            messageContact = itemView.findViewById(R.id.messageContact);
            timeContact = itemView.findViewById(R.id.timeContact);
            itemView.setOnClickListener(this);
        }

        public void setData(User user, int id) {
            this.user = user;
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(user, id);
        }
    }

    private boolean isCurrentDay (String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = new Date(), currentDate = new Date();
        try {
            inputDate = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return inputDate.toString().substring(0, 11).equals(currentDate.toString().substring(0, 11));
    }

    public ContactListAdapter(Context context, OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        if (contacts != null) {
            final Contact current = contacts.get(position);
            String imageData = current.getUser().getProfilePic().substring(21);
            holder.nameContact.setText(current.getUser().getDisplayName());
            byte[] decodedBytes = Base64.decode(imageData, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.picContact.setImageBitmap(decodedBitmap);
            if (current.getLastMessage() != null) {
                holder.messageContact.setText(current.getLastMessage().getContent());
                String time = current.getLastMessage().getCreated();
                if (isCurrentDay(time.substring(0, 10))) {
                    String hour = time.substring(11, 13);
                    hour = String.valueOf((Integer.parseInt(hour) + 3) % 24);
                    time = hour + time.substring(13, 16);
                    holder.timeContact.setText(time);
                } else {
                    holder.timeContact.setText(time.substring(0, 10));
                }
            } else {
                holder.messageContact.setText("");
                holder.timeContact.setText("");
            }
            holder.setData(current.getUser(), current.getContactID());
        }
    }

    public void SetContacts(List<Contact> l) {
        contacts = l;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            return contacts.size();
        } else return 0;
    }
}
