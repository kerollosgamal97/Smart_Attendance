package com.example.smartattendance;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SecondActivity  extends AppCompatActivity {


    //define variables
    Button dateBtn;
    Calendar c;
    DatePickerDialog dpd;
    int dayPicked =0;
    int monthPicked = 0;
    int yearPicked = 0;
    int noOfCards = 0;
    String sectionName = "";
    String sectionNo ="";


    //on create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        //hide menu bar
        getSupportActionBar().hide();


        //prepare dateDiaglog
        dateBtn = (Button) findViewById(R.id.datePicker);
        dateBtn.setOnClickListener(new View.OnClickListener(){


            //calendar code
            // @Override
            public void onClick(View view){
                c = Calendar.getInstance();
                int days = c.get(Calendar.DAY_OF_MONTH);
                int months = c.get(Calendar.MONTH);
                int years = c.get(Calendar.YEAR);


             //Date Picking Code for UI
            dpd = new DatePickerDialog(SecondActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    dayPicked = dayOfMonth;
                    monthPicked = month + 1;
                    yearPicked = year;
                }
            }, years, months,days);
            dpd.show();

            }
    });
    }


     // Go to Camera
    public void goToCamera(View view){


                 //get data
               EditText name = (EditText)findViewById(R.id.sectionname);
               this.sectionName =  name.getText().toString();
                EditText no = (EditText)findViewById(R.id.sectionno);
                this.sectionNo = no.getText().toString();
                Log.i("test", "from"+ 'a'+this.sectionName+'a');
                EditText cardsview = (EditText) findViewById(R.id.noofcards);
                String cards = cardsview.getText().toString();
                if(!cards.isEmpty())
                this.noOfCards = Integer.parseInt(cards);


        //validate all Data
        if(this.yearPicked == 0 || this.dayPicked <= 0 || this.monthPicked <= 0 || this.sectionName.isEmpty() || this.sectionNo.isEmpty()|| this.sectionNo == null || this.sectionName == null  || this.noOfCards <= 0){
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
        }
        else{


            //Go to Camera
            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);


            //pass values
            intent.putExtra("sectionName", this.sectionName);
            intent.putExtra("sectionNo", this.sectionNo);
            intent.putExtra("day", this.dayPicked);
            intent.putExtra("month", this.monthPicked);
            intent.putExtra("year", this.yearPicked);
            intent.putExtra("noofids", this.noOfCards);

           //Go to camera
            startActivity(intent);
            finish();

        }

    }
}
