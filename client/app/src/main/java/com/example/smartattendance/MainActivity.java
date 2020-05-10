package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

     /******** page for only Better UI ********/
     //On create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //hide menu
        getSupportActionBar().hide();


         //move to first activity
        moveToFirstActivity();
    }

    //Move to first Page
    public void moveToFirstActivity(){


        //wait 3 sec
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {


                //get ready to go to first activity
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);


                //move to first activity
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
