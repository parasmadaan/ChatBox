package com.example.paras.chatbox;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class ChatRoom extends AppCompatActivity {

    Button btn_send;
    EditText msg;
    TextView chat;

    String temp_key;

    private String user_name,room_name;

    DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);


     btn_send = (Button) findViewById(R.id.btn_send);
        msg = (EditText) findViewById(R.id.msg_input);
        chat = (TextView) findViewById(R.id.msg);
        user_name = getIntent().getStringExtra("user_name").toString();
        room_name = getIntent().getStringExtra("room_name").toString();
        root = FirebaseDatabase.getInstance().getReference().child(room_name);
        setTitle(" Room : "+room_name);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message = root.child(temp_key);

                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",msg.getText().toString());
                message.updateChildren(map2);
                msg.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    String chat_msg,chat_user;

    private void append_chat(DataSnapshot dataSnapshot) {
        Iterator i =dataSnapshot.getChildren().iterator();
        while (i.hasNext()){
            chat_msg= (String) ((DataSnapshot)i.next()).getValue();
            chat_user= (String) ((DataSnapshot)i.next()).getValue();
            chat.append(chat_user + " : " + chat_msg + "\n");
        }
    }}



