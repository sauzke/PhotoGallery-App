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
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String AUTHORITY = BuildConfig.APPLICATION_ID+".provider";
    String imageFilePath;
    //public static final String EXTRA_MESSAGE = "example text";

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
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(Uri.parse(imageFilePath));
            }
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            ImageView imageVew = findViewById(R.id.imageView);
//            imageVew.setImageBitmap(imageBitmap);
        }
    }

    // When setting button is clicked
    public void settingButton(View view){
        // Sends user to the setting activity
        Intent intent = new Intent(this, DisplaySettingActivity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);

    }

    public void searchButton(View view){
        Intent intent = new Intent(this,DisplaySearchActivity.class);
        startActivity(intent);
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "PhotoGalleryImage_" + timeStamp + "_";
        File storeDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        //System.out.println(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(filename,".jpg",storeDir);

        imageFilePath = image.getAbsolutePath();
        System.out.println(imageFilePath);
        return image;
    }



}
