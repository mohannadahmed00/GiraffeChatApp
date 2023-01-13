package com.example.mychat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter {
    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Message message = (Message) getItem(position);
        if (message.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            View convertView2 = LayoutInflater.from(getContext()).inflate(R.layout.right_message, parent, false);
            ((TextView) convertView2.findViewById(R.C0275id.tv_right_message)).setText(message.getMsg());
            return convertView2;
        }
        View convertView3 = LayoutInflater.from(getContext()).inflate(R.layout.left_message, parent, false);
        ((TextView) convertView3.findViewById(R.C0275id.tv_left_message)).setText(message.getMsg());
        return convertView3;
    }
}
