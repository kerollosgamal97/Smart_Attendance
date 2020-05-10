package com.example.smartattendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {
    // create directory SmartAttendance in pictures and prepare to save in it
    final File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "SmartAttendance");
    public final String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SmartAttendance/";


    //declare variables
    int noofidcards;
    int dayPicked;
    int monthPicked;
    int yearPicked;
    String sectionName;
    String sectionNo;
    int section_id = -1;

    @Override
    //Oncreate
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        //hide menu bar
        getSupportActionBar().hide();
        Intent intent = getIntent();


        // check if directory exists or not
        if (!f.exists()) {
            Log.d("TAG", "Folder doesn't exist, creating it...");
            boolean rv = f.mkdir();
            Log.d("TAG", "Folder creation " + ( rv ? "success" : "failed"));
        } else {
            Log.d("TAG", "Folder already exists.");
        }


        //get ALL variables from pages
          sectionName = intent.getStringExtra("sectionName");
          sectionNo = intent.getStringExtra("sectionNo");
          dayPicked = intent.getIntExtra("day",0);
        monthPicked = intent.getIntExtra("month",0);
         yearPicked = intent.getIntExtra("year",0);
        noofidcards  = intent.getIntExtra("noofids", 0);
        section_id = intent.getIntExtra("secid", -1);


         //request permissions
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
        PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        //create file
        String file = directory + 1 + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //prepare camera
        Uri outputFileUri = Uri.fromFile(newfile);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        //start camera
        startActivityForResult(cameraIntent, 1);
    }


    //redirection to show results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {

            //if all is ok --> pass variables to next page
            Intent tohandle = new Intent(getApplicationContext(), ShowResultsActivity.class);
            tohandle.putExtra("noofids", this.noofidcards);
            tohandle.putExtra("sectionName", this.sectionName);
            tohandle.putExtra("sectionNo", this.sectionNo);
            tohandle.putExtra("day", this.dayPicked);
            tohandle.putExtra("month", this.monthPicked);
            tohandle.putExtra("year", this.yearPicked);
            tohandle.putExtra("secid", this.section_id);

            //go to handle page
            startActivity(tohandle);
            finish();
        }
    }
}
