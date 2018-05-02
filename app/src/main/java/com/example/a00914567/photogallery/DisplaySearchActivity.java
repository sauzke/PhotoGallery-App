package com.example.a00914567.photogallery;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplaySearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
    }

    public void getDates(View view){
        Date startDate = null;
        Date endDate = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        try{
            EditText startDateEditText = findViewById(R.id.startDateView);
            EditText endDateEditText = findViewById(R.id.endDateView);
            startDate = dateFormat.parse(startDateEditText.getText().toString());
            endDate = dateFormat.parse(endDateEditText.getText().toString());

            ArrayList<String> files = getFiles(startDate,endDate);

            Intent data = new Intent();
            data.putExtra("file_list",files);
            setResult(RESULT_OK, data);
            finish();

        }catch(Exception e){

        }
    }

    public ArrayList<String> getFiles(Date startDate, Date endDate) throws ParseException{
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String[] list = dir.list();
        ArrayList<String> result = new ArrayList<String>();

        for(String filename : list){
            String[] parts = filename.split("_");

            String dateString = parts[1].substring(0,4) + "/" + parts[1].substring(4,6) + "/" + parts[1].substring(6,8);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date pictureDate = dateFormat.parse(dateString);

            if(pictureDate.compareTo(startDate) >= 0 && pictureDate.compareTo(endDate) <= 0){
                result.add(filename);
            }
        }

        return result;
    }
}
