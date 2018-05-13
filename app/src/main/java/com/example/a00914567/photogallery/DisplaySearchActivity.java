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
import java.util.Scanner;

import static com.example.a00914567.photogallery.PhotoMange.FileManager.*;

public class DisplaySearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search);
    }

    public void getResults(View view){
        Date startDate = null;
        Date endDate = null;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        EditText startDateEditText = findViewById(R.id.startDateView);
        EditText endDateEditText = findViewById(R.id.endDateView);
        EditText keywordEditText = findViewById(R.id.keywordEditText);

        if((!startDateEditText.getText().toString().trim().matches("") && !endDateEditText.getText().toString().trim().matches("")) || !keywordEditText.getText().toString().trim().matches("")) {
            try {
                if(!keywordEditText.getText().toString().trim().matches("")){
                    File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

                    String keyword = keywordEditText.getText().toString().trim();

                    ArrayList<String> files = getPictureByKeyword(dir, keyword);

                    Intent data = new Intent();
                    data.putExtra("file_list", files);
                    setResult(RESULT_OK, data);
                    finish();
                }
                else {
                    startDate = dateFormat.parse(startDateEditText.getText().toString());
                    endDate = dateFormat.parse(endDateEditText.getText().toString());
                    File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                    ArrayList<String> files = getPictureByDate(dir, startDate, endDate);

                    Intent data = new Intent();
                    data.putExtra("file_list", files);
                    setResult(RESULT_OK, data);
                    finish();
                }

            } catch (Exception e) {

            }
        }
    }

}
