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
                    File commentData = new File(dir.getAbsolutePath() + "/PhotoGallery.dat");

                    if(commentData.exists()) {
                        Scanner scanner = new Scanner(commentData);
                        ArrayList<String> files = new ArrayList<String>();

                        while (scanner.hasNextLine()) {
                            String temp = scanner.nextLine();
                            String[] parts = temp.split(";");

                            if (parts[1].matches(keywordEditText.getText().toString().trim())) {
                                files.add(parts[0]);
                            }
                        }

                        Intent data = new Intent();
                        data.putExtra("file_list", files);
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
                else {
                    startDate = dateFormat.parse(startDateEditText.getText().toString());
                    endDate = dateFormat.parse(endDateEditText.getText().toString());

                    ArrayList<String> files = getFiles(startDate, endDate);

                    Intent data = new Intent();
                    data.putExtra("file_list", files);
                    setResult(RESULT_OK, data);
                    finish();
                }

            } catch (Exception e) {

            }
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
