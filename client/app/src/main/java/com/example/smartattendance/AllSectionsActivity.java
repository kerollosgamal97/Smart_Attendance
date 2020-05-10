package com.example.smartattendance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class AllSectionsActivity extends AppCompatActivity {

    //define lists
    ArrayList<Integer> ids = new ArrayList<Integer>();
    ArrayList<String> data = new ArrayList<String>();


    //On create
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_sections);


        //hide menu bar
        getSupportActionBar().hide();


        try {


            //Connect to database
            SQLiteDatabase mydb = this.openOrCreateDatabase("Attendance", MODE_PRIVATE, null);
            mydb.execSQL("PRAGMA foreign_keys = ON");
            mydb.execSQL("CREATE TABLE IF NOT EXISTS section(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " sectionName VARCHAR, secNO INT(5)" +
                    ", day INT(2)" +
                    ", month INT(2)" +
                    ", year INT(5)  )");
            mydb.execSQL("CREATE TABLE IF NOT EXISTS session(SEC_id INTEGER ," +
                    "student_id VARCHAR NOT NULL," +
                    "FOREIGN KEY(SEC_id) REFERENCES section(id) ON DELETE CASCADE," +
                    "PRIMARY KEY (SEC_id, student_id))");
            mydb.execSQL("DELETE FROM section where sectionName = 'Big Data'");


            //Select all data to be shown
            Cursor c = mydb.rawQuery("SELECT * FROM section", null);
            int idIndex = c.getColumnIndexOrThrow("id");
            int nameIndex = c.getColumnIndexOrThrow("sectionName");
            int noIndex = c.getColumnIndexOrThrow("secNO");
            int dayIndex = c.getColumnIndexOrThrow("day");
            int monthIndex = c.getColumnIndexOrThrow("month");
            int yearIndex = c.getColumnIndexOrThrow("year");

            c.moveToFirst();

            do {

                //add sections to list
                ids.add(c.getInt(idIndex));
                data.add("\r\n"+"Subject Name : " + c.getString(nameIndex) + " \r\n \r\n" +
                        "Section Number : " +Integer.toString(c.getInt(noIndex)) +"\r\n \r\n"+
                        "Date : " + Integer.toString(c.getInt(dayIndex)) + " / "+
                        Integer.toString(c.getInt(monthIndex)) + " / "+
                        Integer.toString(c.getInt(yearIndex)) + "\r\n ");
            } while (c.moveToNext());


            //show ids in a list
            ListView sec = (ListView) findViewById(R.id.Sections);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, data);
            sec.setAdapter(arrayAdapter);


            //on item click-->Go to section info
            sec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //go to selected section and pass id
                    Intent intent = new Intent(getApplicationContext(),SelectedSectionActivity.class );
                    intent.putExtra("id", ids.get(position));
                    startActivity(intent);
                }
            });

        }
            catch (Exception e){
                e.printStackTrace();

        }

    }
}

