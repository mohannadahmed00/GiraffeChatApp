package com.example.mychat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    com.example.mychat.MessageAdapter adapter;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    EditText etMsg;
    String friendID;
    ImageView ivSendMsg;
    ListView lvMessages;
    Message message;
    ArrayList<Message> messages;
    DatabaseReference messagesRef;
    String myID;
    TextView tvSignOut;
    DatabaseReference usersRef;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main) ;
        this.auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        this.databaseReference = reference;
        this.usersRef = reference.child("user info");
        this.messagesRef = this.databaseReference.child("chat");
        this.myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.usersRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!ds.getKey().equals(com.example.mychat.MainActivity.this.myID)) {
                        com.example.mychat.MainActivity.this.friendID = ds.getKey();
                    }
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
        this.lvMessages = (ListView) findViewById(R.id.lv_messages_main_activity);
        this.tvSignOut = (TextView) findViewById(R.id.tv_sign_out_main_activity);
        this.ivSendMsg = (ImageView) findViewById(R.id.iv_send_message_main_activity);
        this.etMsg = (EditText) findViewById(R.id.et_message_main_activity);
        this.tvSignOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.example.mychat.MainActivity.this.auth.signOut();
                Intent intent = new Intent(com.example.mychat.MainActivity.this, com.example.mychat.LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        this.ivSendMsg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                com.example.mychat.MainActivity.this.message = new Message(com.example.mychat.MainActivity.this.myID, com.example.mychat.MainActivity.this.friendID, com.example.mychat.MainActivity.this.etMsg.getText().toString().trim());
                com.example.mychat.MainActivity.this.messagesRef.push().setValue(com.example.mychat.MainActivity.this.message);
                com.example.mychat.MainActivity.this.etMsg.setText("");
            }
        });
        readMsg();
        this.ivSendMsg.setVisibility(View.GONE);
        this.etMsg.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (com.example.mychat.MainActivity.this.etMsg.getText().length() > 0) {
                    com.example.mychat.MainActivity.this.ivSendMsg.setVisibility(View.VISIBLE);
                } else {
                    com.example.mychat.MainActivity.this.ivSendMsg.setVisibility(View.GONE);
                }
            }

            public void afterTextChanged(Editable s) {
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void readMsg() {
        this.messages = new ArrayList<>();
        this.messagesRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                com.example.mychat.MainActivity.this.messages.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    com.example.mychat.MainActivity.this.messages.add(new Message((String) ds.child("sender").getValue(String.class), (String) ds.child("receiver").getValue(String.class), (String) ds.child(NotificationCompat.CATEGORY_MESSAGE).getValue(String.class)));
                }
                com.example.mychat.MainActivity mainActivity = com.example.mychat.MainActivity.this;
                com.example.mychat.MainActivity mainActivity2 = com.example.mychat.MainActivity.this;
                mainActivity.adapter = new com.example.mychat.MessageAdapter(mainActivity2, mainActivity2.messages);
                com.example.mychat.MainActivity.this.lvMessages.setAdapter(com.example.mychat.MainActivity.this.adapter);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
