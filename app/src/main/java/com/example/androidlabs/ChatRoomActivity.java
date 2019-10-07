package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ChatRoomActivity extends AppCompatActivity
{
   private ListView chatWindow;
    private ArrayList<Message> messages;

    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4_main_chatroom);

        messages =new ArrayList<>();
        chatWindow= findViewById(R.id.list_Lab4);
        ChatAdapter adapter =new ChatAdapter(messages);
        Button sendBtn = findViewById(R.id.SendBt);
        Button receiveBtn = findViewById(R.id.receiveBt);
        chatWindow.setAdapter(adapter);



        sendBtn.setOnClickListener(e -> {
            EditText editChat = findViewById(R.id.ChatEdit);
            if(!(editChat.getText().toString().equals(""))){
                messages.add(new Message(editChat.getText().toString(),true));
                adapter.notifyDataSetChanged();
                editChat.setText("");
            }
        });

        receiveBtn.setOnClickListener(e -> {
            EditText editChat = findViewById(R.id.ChatEdit);
            if(!(editChat.getText().toString().equals(""))){
                messages.add(new Message(editChat.getText().toString(), false));
                adapter.notifyDataSetChanged();
                editChat.setText("");
            }
        });
    }

        protected class ChatAdapter extends BaseAdapter {

            private ArrayList<Message> messages;

            protected ChatAdapter(ArrayList chatArray){
                this.messages = chatArray;
            }

            @Override
            public int getCount(){
                return messages.size();
            }

            @Override
            public Object getItem(int position){
                return messages.get(position);
            }

            @Override
            public long getItemId(int position){
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                LayoutInflater inflater = getLayoutInflater();
                String message = messages.get(position).getMessage();
                boolean sent = messages.get(position).isSent();
                View chatView;

                if(sent) {
                    chatView = inflater.inflate(R.layout.activity_main_lab4_send, parent, false);
                }
                else {
                    chatView = inflater.inflate(R.layout.activity_main_lab4_receive, parent, false);
                }

                RelativeLayout layout = chatView.findViewById(R.id.layout);
                TextView text = (TextView)layout.getChildAt(1);
                text.setText(message);
                return layout;
            }
        }
}
