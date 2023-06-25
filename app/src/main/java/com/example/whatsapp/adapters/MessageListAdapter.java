package com.example.whatsapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.R;
import com.example.whatsapp.entities.Message;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView messageBody;
        private final TextView messageTime;
        private MessageViewHolder (View itemView) {
            super(itemView);
            messageBody = itemView.findViewById(R.id.messageBody);
            messageTime = itemView.findViewById(R.id.messageTime);
        }
    }

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    private List<Message> messages;
    private final String username;
    private final LayoutInflater mInflater;
    public MessageListAdapter (Context context, String username) {
        mInflater = LayoutInflater.from(context);
        this.username = username;
    }

    @Override
    public int getItemViewType(int position) {
        Message current = messages.get(position);
        return current.getSender().getName().equals(username) ? VIEW_TYPE_RECEIVED : VIEW_TYPE_SENT;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == VIEW_TYPE_SENT) {
            itemView = mInflater.inflate(R.layout.send_message, parent, false);
        } else {
            itemView = mInflater.inflate(R.layout.get_message, parent, false);
        }
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        if (messages != null) {
            final Message current = messages.get(position);
            holder.messageBody.setText(current.getContent());
            String time, hour = current.getCreated().substring(11, 13);
            hour = String.valueOf((Integer.parseInt(hour) + 3) % 24);
            time = hour + current.getCreated().substring(13, 16);
            holder.messageTime.setText(time);
        }
    }
    public void SetMessages(List<Message> l) {
        messages = l;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (messages != null) {
            return messages.size();
        }
        else return 0;
    }
}
