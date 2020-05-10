package com.example.smartattendance;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;

public class ShowResultsActivity extends AppCompatActivity {


    //variable declaration
    int noofidcards;
    int dayPicked;
    int monthPicked;
    int yearPicked;
    String sectionName;
    String sectionNo;
    int section_id = -1;
    ArrayList<String> result;

     // on create fn
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        //hide menu
        getSupportActionBar().hide();

        //get variables from other pages
        Intent intent = getIntent();
        sectionName = intent.getStringExtra("sectionName");
        sectionNo = intent.getStringExtra("sectionNo");
        dayPicked = intent.getIntExtra("day", 0);
        monthPicked = intent.getIntExtra("month", 0);
        yearPicked = intent.getIntExtra("year", 0);
        noofidcards = intent.getIntExtra("noofids", 0);
        section_id = intent.getIntExtra("secid", -1);


        //start async thread to connect with server
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // define file path
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartAttendance/" + "1.jpg";


        //extract data from txt to file then show
        result = new ArrayList<>();
        String txtfile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartAttendance/" + "output.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(txtfile))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                result.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //list view to show items
        ListView myview = (ListView) findViewById(R.id.test);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, result);
        myview.setAdapter(arrayAdapter);


    }

    //advance to next page
    public void advanceNext(View view) {

        //send data and go to handleimage
        Intent intent = new Intent(getApplicationContext(), HandleImageActivity.class);
        intent.putExtra("sectionName", this.sectionName);
        intent.putExtra("sectionNo", this.sectionNo);
        intent.putExtra("day", this.dayPicked);
        intent.putExtra("month", this.monthPicked);
        intent.putExtra("year", this.yearPicked);
        intent.putExtra("secid", this.section_id);
        startActivity(intent);
        finish();

    }

    public void saveAndAdvance(View view) {


        //save code
        //first time  -->just insert needed data
        if (this.section_id == -1) {
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

                //enter variable values to database section
                mydb.execSQL("INSERT INTO section(sectionName,secNO,day,month,year)  VALUES ('"+this.sectionName+ "'," + this.sectionNo + "," + this.dayPicked + "," + this.monthPicked + "," + this.yearPicked +")");


                //get last data entered in database session
                Cursor c = mydb.rawQuery("SELECT id FROM section ORDER BY id DESC LIMIT 1", null);
                c.moveToFirst();
                int last_id = c.getInt(0);


                //enter arraylist result in database
                for (int i = 0; i < result.size(); i++) {
                    mydb.execSQL("INSERT INTO session (SEC_id, student_id) VALUES (" + last_id + "," + result.get(i) + ")");
                    this.section_id = last_id;

                }

            }
            catch(Exception e){
                e.printStackTrace();
                }
                advanceNext(view);

            }
        else{ //if not first time --> save id and advance


            //connect to database
            SQLiteDatabase mydb = this.openOrCreateDatabase("Attendance", MODE_PRIVATE, null);
            mydb.execSQL("PRAGMA foreign_keys = ON");
            mydb.execSQL("CREATE TABLE IF NOT EXISTS section(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    " sectionName VARCHAR, secNO INT(5)" +
                    ", day INT(2)" +
                    ", month INT(2)" +
                    ", year INT(5)  )");


            //get last id entered
            Cursor c = mydb.rawQuery("SELECT id FROM section ORDER BY id DESC LIMIT 1", null);
            c.moveToFirst();
            int last_id = c.getInt(0);

            //enter arraylist items in database
            for (int i = 0; i < result.size(); i++) {
                mydb.execSQL("INSERT INTO session (SEC_id, student_id) VALUES (" + last_id + "," + result.get(i) + ")");
                this.section_id = last_id;

            }
            advanceNext(view);
        }


        }


        //create new thread for server connection
        Thread thread = new Thread(new Runnable() {


            //variables declaration
            byte[] image_buffer = new byte[1024];
            Socket sock;
            Socket client = null;
            FileOutputStream fos = null;
            DataOutputStream dos = null;
            DataInputStream dis = null;
            FileInputStream fis = null;


            //method to sconnect through socket programming
            @Override
            public void run() {
                try {


                    //Configure IP address and port number
                    String ServerIP = "192.168.1.2";
                    int PortNumber = 8800;


                    //Connection with server and define streams
                    Socket client = null;
                    client = new Socket(ServerIP, PortNumber);
                    dos = new DataOutputStream(client.getOutputStream());
                    dis = new DataInputStream(client.getInputStream());


                    //Read image
                    String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartAttendance/" + "1.jpg";
                    File image = new File(file_path);


                    //compress image
                    File compressedImageFile = new Compressor(getBaseContext()).compressToFile(image);
                    FileInputStream fis = new FileInputStream(compressedImageFile);


                    //send file size
                    dos.writeBytes(String.valueOf(compressedImageFile.length()) + "+"+String.valueOf(noofidcards) );
                    dos.flush();


                    //Send image to server
                    while (fis.read(image_buffer) != -1) {
                        dos.write(image_buffer);
                        dos.flush();
                    }


                    // Receive output from server
                    String outputfile_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartAttendance/" + "output.txt";
                    File output = new File(outputfile_path);
                    fos = new FileOutputStream(output);
                    byte[] buffer = new byte[4096];
                    dis.read(buffer);


                    //enter needed data only in file
                    int i = 0;
                    while (i < 2048) {
                        if (buffer[i] >= 97 && buffer[i] <= 122 || buffer[i] == 13 || buffer[i] <= 90 && buffer[i] >= 65 || buffer[i] >= 48 && buffer[i] <= 57) {
                            fos.write(buffer[i]);
                            fos.flush();
                        }
                        i++;
                    }


                    //close file and end socket connection
                    fos.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
}


















