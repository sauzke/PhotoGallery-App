package com.example.a00914567.photogallery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static com.example.a00914567.photogallery.PhotoMange.FileManager.createImageFile;
import static com.example.a00914567.photogallery.PhotoMange.PhotoDetailsManager.*;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int START_TO_SEARCH = 2;
    public static final int START_TO_SETTING = 3;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    String imageFilePath;
    ArrayList<String> list = new ArrayList<String>();
    int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // When the snap button is clicked
    public void takePicture(View view) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photoFile = createImageFile(dir);
                imageFilePath = photoFile.getName();
            } catch (IOException e) {
                // Catch exception
            }

            if (photoFile != null) {
                //ImageView image = findViewById(R.id.imageView);

                Uri photoURI = FileProvider.getUriForFile(this, AUTHORITY, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Recieving activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fileDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        ImageView imageView = findViewById(R.id.imageView);
        TextView detail = findViewById(R.id.imageDetailTextView);
        EditText captions = findViewById(R.id.captionEditText);
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String pictureUri = dir.getAbsolutePath() + "/" + imageFilePath;

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setLocation(this, pictureUri);

            imageView.setImageURI(Uri.parse(pictureUri));

            list.add(imageFilePath);
            currentIndex = list.size() - 1;

            updatePictureDetails(pictureUri, detail, captions, geocoder, fileDir, dir);

            unHideCaptions();
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode != RESULT_OK) {
            System.out.println("photo not taken");
        }
        if (requestCode == START_TO_SEARCH && resultCode == RESULT_OK) {
            list = (ArrayList<String>) data.getSerializableExtra("file_list");

            if (!list.isEmpty()) {
                pictureUri = dir.getAbsolutePath() + "/" + list.get(0);
                imageView.setImageURI(Uri.parse(pictureUri));
                updatePictureDetails(pictureUri, detail, captions, geocoder, fileDir, dir);
                currentIndex = 0;

                unHideCaptions();
            }
        }
    }

    // When setting button is clicked
    public void settingButton(View view) {
        Intent intent = new Intent(this, DisplaySettingActivity.class);
        startActivity(intent);
    }

    // When search button is clicked
    public void searchButton(View view) {
        Intent intent = new Intent(this, DisplaySearchActivity.class);
        startActivityForResult(intent, START_TO_SEARCH);
    }

    // When left button is clicked
    public void leftButton(View view) {
        if (currentIndex > 0 && !list.isEmpty()) {
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File fileDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            ImageView imageView = findViewById(R.id.imageView);
            EditText captions = findViewById(R.id.captionEditText);
            TextView detail = findViewById(R.id.imageDetailTextView);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            if (!captions.getText().toString().trim().matches("")) {
                try {
                    setComment(list.get(currentIndex), captions.getText().toString(), fileDir);
                } catch (IOException e) {

                }
            }

            currentIndex--;

            String pictureUri = dir.getAbsolutePath() + "/" + list.get(currentIndex);
            imageView.setImageURI(Uri.parse(pictureUri));
            updatePictureDetails(pictureUri, detail, captions, geocoder, fileDir, dir);
        }
    }

    // When right button is clicked
    public void rightButton(View view) {
        if (list.size() > currentIndex + 1) {
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File fileDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            ImageView imageView = findViewById(R.id.imageView);
            EditText captions = findViewById(R.id.captionEditText);
            TextView detail = findViewById(R.id.imageDetailTextView);
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            if (!captions.getText().toString().trim().matches("")) {
                try {
                    setComment(list.get(currentIndex), captions.getText().toString(), fileDir);
                } catch (IOException e) {

                }
            }

            currentIndex++;

            String pictureUri = dir.getAbsolutePath() + "/" + list.get(currentIndex);
            imageView.setImageURI(Uri.parse(pictureUri));
            updatePictureDetails(pictureUri, detail, captions, geocoder, fileDir, dir);
        }
    }

     public void unHideCaptions() {
        EditText captions = findViewById(R.id.captionEditText);
        if (!captions.isShown()) {
            captions.setVisibility(View.VISIBLE);
        }
    }
}
