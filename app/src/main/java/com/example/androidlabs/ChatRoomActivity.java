package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.ConversationActions;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import static com.example.androidlabs.MyDatabaseOpenHelper.*;


public class ChatRoomActivity extends AppCompatActivity
{
    private ListView chatWindow;
    private ArrayList<Message> messages;
    SQLiteDatabase db;
    Cursor result;

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

        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        // set data from db
        String [] columns = { MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_ISSEND};
        result = db.query( MyDatabaseOpenHelper.TABLE_NAME, columns, null,null,null,null,null,null);

        //Get column indexes
        int idConIndex = result.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int nameColIndex = result.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int isSentConIndex = result.getColumnIndex(MyDatabaseOpenHelper.COL_ISSEND);

        while (result.moveToNext())
        {
            long id = result.getLong(idConIndex);
            String name = result.getString(nameColIndex);
            int isSent = result.getInt(isSentConIndex);

            messages.add(new Message(id,name,isSent==1));
            adapter.notifyDataSetChanged();
        }

        sendBtn.setOnClickListener(e -> {

            EditText editChat = findViewById(R.id.ChatEdit);
            if(!(editChat.getText().toString().equals("")))
            {
                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE,editChat.getText().toString());
                newRowValues.put(MyDatabaseOpenHelper.COL_ISSEND, 1);
                long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

                messages.add(new Message( newId,editChat.getText().toString(), true));

                chatWindow.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                editChat.setText("");
                this.printCursor();
            }
        });

        receiveBtn.setOnClickListener(e -> {
            EditText editChat = findViewById(R.id.ChatEdit);

            if(!(editChat.getText().toString().equals("")))
           {

               ContentValues newRowValues = new ContentValues();
               newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE,editChat.getText().toString());
               newRowValues.put(MyDatabaseOpenHelper.COL_ISSEND, 0);
               long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

               messages.add(new Message(newId,editChat.getText().toString(), false));

               chatWindow.setAdapter(adapter);
               adapter.notifyDataSetChanged();
               editChat.setText("");
               this.printCursor();
           }
        });
       printCursor();
    }

    protected class ChatAdapter extends BaseAdapter
    {
        private ArrayList<Message> messages;

        private ChatAdapter(ArrayList chatArray)
        {
            this.messages = chatArray;
        }

        @Override
        public int getCount()
        {
            return messages.size();
        }

        @Override
        public Object getItem(int position)
        {
            return messages.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            LayoutInflater inflater = getLayoutInflater();
           String message = messages.get(position).getMessage();
            boolean sent = messages.get(position).isSent();
            View chatView = convertView;

           Message thisMessageRow = messages.get(position);

                if(thisMessageRow.isSent())
                {
                    chatView = inflater.inflate(R.layout.activity_main_lab4_send,parent,false);
                    //TextView itemField = chatView.findViewById(R.id.layout);
                }
            else
            {
                chatView = inflater.inflate(R.layout.activity_main_lab4_receive, parent, false);
            }
            //TextView messageRow = chatView.findViewById(R.id.layout);

            RelativeLayout layout = chatView.findViewById(R.id.layout);
            TextView text = (TextView)layout.getChildAt(1);

            //  text.setText(thisMessageRow.getMessage());
            text.setText(message);
            return chatView;

        }
    }

    public void printCursor() {

        Log.e("MyDatabaseFile version:", db.getVersion() + "");

        Log.e("Number of columns:", result.getColumnCount() + "");

        Log.e("Name of the columns:", Arrays.toString(result.getColumnNames()));

        Log.e("Number of results", result.getCount() + "");

        Log.e("Each row of results :", "");

        result.moveToFirst();


        for (int i = 0; i < result.getCount(); i++) {

            while (!result.isAfterLast())
            {
                Log.e("id", result.getString(result.getColumnIndex(COL_ID)) + "");

                Log.e("isSent", result.getString(result.getColumnIndex(COL_ISSEND)) + "");

                Log.e("message", result.getString(result.getColumnIndex(COL_MESSAGE)) + "");

                result.moveToNext();
            }
        }
    }


}

