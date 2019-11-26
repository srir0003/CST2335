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
import android.content.Intent;


public class ChatRoomActivity extends AppCompatActivity
{
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String LIST_ITEM_ID = "LIST_ID";
    public static final String DB_ITEM_ID = "DB_ID";
    public static final String MESSAGE = "MESSAGE";
    public static final String IS_SENT = "IS_SENT";
    public static final String IS_RECEIVED = "IS_RECEIVED";
    public static final int EMPTY_ACTIVITY = 345;

    private ListView chatWindow;
    private ArrayList<Message> messages;
    Message newMessage;
    SQLiteDatabase db;
    EditText chat;
    BaseAdapter adapter;
    Cursor result;

    @Override
    protected void onCreate( Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4_main_chatroom);

        messages =new ArrayList<>();
        chatWindow= findViewById(R.id.list_Lab4);
        adapter =new ChatAdapter(messages);
        Button sendBtn = findViewById(R.id.SendBt);
        Button receiveBtn = findViewById(R.id.receiveBt);
        chatWindow.setAdapter(adapter);

        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();

        // set data from db
        String [] columns = { MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_ISSEND, MyDatabaseOpenHelper.COL_RECEIVED};
        result = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null,null,null,null,null,null);

        //Get column indexes
        int idConIndex = result.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int nameColIndex = result.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int isSentConIndex = result.getColumnIndex(MyDatabaseOpenHelper.COL_ISSEND);
        int isReceivedColIndex = result.getColumnIndex(MyDatabaseOpenHelper.COL_RECEIVED);

        while (result.moveToNext())
        {
            long id = result.getLong(idConIndex);
            String text = result.getString(nameColIndex);
            String sent = result.getString(isSentConIndex);
            String received = result.getString(isReceivedColIndex);


            if (sent.equals("1"))
            {
                messages.add(new Message(id ,text,true,false));

            } else if (received.equals("1"))
            {
                messages.add(new Message(id, text, false, true));

            } else {

            }
        }


        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
        chatWindow.setOnItemClickListener((lv, vw, pos, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(MESSAGE, messages.get(pos).getMessage());
            dataToPass.putLong(LIST_ITEM_ID, id);
            dataToPass.putLong(DB_ITEM_ID, messages.get(pos).getId());
            dataToPass.putBoolean(IS_SENT,messages.get(pos).getSent());
            dataToPass.putBoolean(IS_RECEIVED, messages.get(pos).getReceive());

            if (isTablet)
            {
                ChatDetailFragment dFragment = new ChatDetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.

            }else
                {
                    Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                    nextActivity.putExtras(dataToPass); //send data to next activity
                    startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
                }

        });

        sendBtn.setOnClickListener(click -> {

            EditText editChat = findViewById(R.id.ChatEdit);
            if(!(editChat.getText().toString().equals("")))
            {
                String text = editChat.getText().toString();
                String isSent = "1";
                String isReceived = "0";

                ContentValues newRowValues = new ContentValues();
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE,text);
                newRowValues.put(MyDatabaseOpenHelper.COL_ISSEND,isSent);
                newRowValues.put(COL_RECEIVED,isReceived);
                long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

                messages.add(new Message( newId,editChat.getText().toString(),true, false));

                chatWindow.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                editChat.setText("");
                editChat.getText().clear();
                this.printCursor();
            }
        });

        receiveBtn.setOnClickListener(click -> {
            EditText editChat = findViewById(R.id.ChatEdit);

            if(!(editChat.getText().toString().equals("")))
           {

               ContentValues newRowValues = new ContentValues();
               newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE,editChat.getText().toString());
               newRowValues.put(MyDatabaseOpenHelper.COL_ISSEND, 0);
               newRowValues.put(COL_RECEIVED,1);
               long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);

               messages.add(new Message(newId,editChat.getText().toString(), false,true));

               chatWindow.setAdapter(adapter);
               adapter.notifyDataSetChanged();
               editChat.setText("");
               editChat.getText().clear();
               this.printCursor();
           }
        });
       printCursor();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EMPTY_ACTIVITY)
        {
            if(resultCode == RESULT_OK) //if you hit the delete button instead of back button
                {
                    long msgId = data.getLongExtra(LIST_ITEM_ID, 0);
                    long dbId = data.getLongExtra(DB_ITEM_ID, 0);
                    deleteMessageId(msgId,dbId);
                }
        }
    }

    public void deleteMessageId(long msgId, long dbId)
    {
        Log.i("Delete this message:" , "msg id="+msgId + " database id" + dbId);
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, "id=?", new String[]{Long.toString(dbId)});
        messages.remove((int)msgId);
        adapter.notifyDataSetChanged();
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
            boolean sent = messages.get(position).getSent();
            View chatView = convertView;

           Message thisMessageRow = messages.get(position);

                if(thisMessageRow.getSent())
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
                Log.e("isReceive",result.getString(result.getColumnIndex(COL_RECEIVED)) + "");

                result.moveToNext();
            }
        }
    }


}

