package com.example.smartattendance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SelectedSectionActivity extends AppCompatActivity {

    //List for received ids
    ArrayList<String> s_ids = new ArrayList<String>();
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_section);


        //hide menu bar
        getSupportActionBar().hide();

        //Receive ids
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
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


            //select last id from database
            Cursor c = mydb.rawQuery("SELECT student_id FROM session WHERE SEC_id = "+ id , null);
            int studenIndex = c.getColumnIndexOrThrow("student_id");
            c.moveToFirst();


            // put ids in the list
            do{
                s_ids.add(c.getString(studenIndex));

            }while (c.moveToNext());


            //Show ids in arraylist
            ListView lv = (ListView) findViewById(R.id.viewStudents);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1, s_ids);
            lv.setAdapter(arrayAdapter);
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }


    //Delete section fns
    public void deleteSection(View view){

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


        //Delete selected section from tables
        mydb.execSQL("DELETE FROM session WHERE SEC_id = " + this.id);
        mydb.execSQL("DELETE FROM section WHERE id = " + this.id);


        //return to first page
        Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
        startActivity(intent);
        finish();
    }

}
