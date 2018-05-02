package com.example.a00914567.photogallery;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int START_TO_SEARCH = 2;
    public static final int START_TO_SETTING = 3;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID+".provider";
    String imageFilePath;
    ArrayList<String> list = new ArrayList<String>();
    int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void takePicture(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException e){
                // Catch exception
            }

            if (photoFile != null) {
                ImageView image = findViewById(R.id.imageView);

                Uri photoURI = FileProvider.getUriForFile(this, AUTHORITY, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(Uri.parse(imageFilePath));

            list.add(imageFilePath);
            currentIndex = list.size() - 1;

            System.out.println("size: " + list.size() + ", index: " + currentIndex);
        }
        if(requestCode == START_TO_SEARCH && resultCode == RESULT_OK){
            list = (ArrayList<String>) data.getSerializableExtra("file_list");

            if(!list.isEmpty()){
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(Uri.parse(dir.getAbsolutePath() + "/" + list.get(0)));
                currentIndex = 0;
            }
        }
    }

    // When setting button is clicked
    public void settingButton(View view){
        Intent intent = new Intent(this, DisplaySettingActivity.class);
        startActivity(intent);
    }

    public void searchButton(View view){
        Intent intent = new Intent(this,DisplaySearchActivity.class);
        startActivityForResult(intent,START_TO_SEARCH);
    }

    private File createImageFile() throws IOException{
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "PhotoGalleryImage_" + timeStamp + "_";
        File image = File.createTempFile(filename,".jpg",dir);

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    public void leftButton(View view){
        if(currentIndex > 0 && !list.isEmpty()){
            currentIndex--;
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(Uri.parse(dir.getAbsolutePath() + "/" + list.get(currentIndex)));
        }
    }

    public void rightButton(View view){
        if(list.size() > currentIndex + 1){
            currentIndex++;
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            ImageView imageView = findViewById(R.id.imageView);
            imageView.setImageURI(Uri.parse(dir.getAbsolutePath() + "/" + list.get(currentIndex)));
        }
    }
}
