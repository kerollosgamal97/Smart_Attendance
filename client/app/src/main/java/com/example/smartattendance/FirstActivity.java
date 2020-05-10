package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    //On create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        //hide menu bar
        getSupportActionBar().hide();
    }


    //go to second page
    public void goToSecondActivity(View view){
        Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
        startActivity(intent);
    }

    //go to sections page
    public void goToSections(View view){
        Intent intent = new Intent(getApplicationContext(),AllSectionsActivity.class);
        startActivity(intent);
    }


}
