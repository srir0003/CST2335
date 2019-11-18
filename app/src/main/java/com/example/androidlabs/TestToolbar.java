package com.example.androidlabs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import android.content.DialogInterface;



public class TestToolbar extends AppCompatActivity
{
    private Toolbar testTB;
    private String message = "This is the initial message";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        testTB =findViewById(R.id.testToolbar);
       // setSupportActionBar(testTB);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()){
            case R.id.choice1:
                Toast.makeText(this,message ,Toast.LENGTH_SHORT).show();
                break;

            case R.id.choice2:
                alert();
                break;

                case R.id.choice3:
                    Snackbar.make(testTB, "Go Back?", Snackbar.LENGTH_SHORT)
                            .setAction("Going Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                }
                            })
                            .show();
                    break;

            case R.id.choice4:
                Toast.makeText(this,"You clicked on the overflow menu",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    public void alert()
    {
        View middle = this.getLayoutInflater().inflate(R.layout.activity_alert_mess, null);
        EditText newMessage = middle.findViewById(R.id.alertEV);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("Positive", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        message = newMessage.getText().toString();

                    }
                })

                .setNegativeButton("Negative", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }

                }).setView(middle);

        builder.create().show();
    }
}
