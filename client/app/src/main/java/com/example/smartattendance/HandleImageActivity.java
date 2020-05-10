package com.example.smartattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HandleImageActivity extends AppCompatActivity {

    //define variables
     String sectionName;
     String sectionNo;
     int dayPicked;
     int monthPicked;
     int yearPicked;
     int section_id;


     //On create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_image);


        //hide menu
        getSupportActionBar().hide();
        Intent intent = getIntent();


        // expected no of cards
        int expectedNoOfCards =  intent.getIntExtra("noofidcards", 0);
        sectionName = intent.getStringExtra("sectionName");
        sectionNo = intent.getStringExtra("sectionNo");
        dayPicked = intent.getIntExtra("day", 0);
        monthPicked = intent.getIntExtra("month", 0);
        yearPicked = intent.getIntExtra("year", 0);
        section_id = intent.getIntExtra("secid", -1);
    }


    //back to take another photo
    public  void backToCamera(View view){
        EditText ids = (EditText) findViewById(R.id.idNo);
        String idno = ids.getText().toString();
        if(!idno.isEmpty() && Integer.parseInt(idno) != 0 ) {


            //Pass variables to camera again
            Intent backToCamera = new Intent(getApplicationContext(), CameraActivity.class);
            backToCamera.putExtra("noofids", Integer.parseInt(idno));
            backToCamera.putExtra("sectionName", this.sectionName);
            backToCamera.putExtra("sectionNo", this.sectionNo);
            backToCamera.putExtra("day", this.dayPicked);
            backToCamera.putExtra("month", this.monthPicked);
            backToCamera.putExtra("year", this.yearPicked);
            backToCamera.putExtra("secid", this.section_id);


            //go to camera
            startActivity(backToCamera);
            finish();
        }
        else
        {

            //error message
            Toast.makeText(this, "Please Enter Expected no of id cards", Toast.LENGTH_SHORT).show();
        }
    }


    //back to first activity
    public void backToHome(View view){
        Intent backtohome = new Intent(getApplicationContext(), FirstActivity.class);
        startActivity(backtohome);
        finish();
    }


    //show results
    public void gotoshowresults(){
        Intent intent = new Intent(getApplicationContext(), ShowResultsActivity.class);
        startActivity(intent);
    }
}
