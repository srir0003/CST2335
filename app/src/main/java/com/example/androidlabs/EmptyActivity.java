package com.example.androidlabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


public class EmptyActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle dataToPass =getIntent().getExtras();

        ChatDetailFragment dFragment = new ChatDetailFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment)
                .commit();

    }
}
