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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    // When the snap button is clicked
    public void takePicture(View view) {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException e){
                // Catch exception
            }

            if (photoFile != null) {
                //ImageView image = findViewById(R.id.imageView);

                Uri photoURI = FileProvider.getUriForFile(this, AUTHORITY, photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    // Recieving activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            ImageView imageView = findViewById(R.id.imageView);
            String pictureUri = dir.getAbsolutePath() + "/" +imageFilePath;

            if(ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                if (locationManager != null) {
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if(location != null){
                        try {
                            ExifInterface exif = new ExifInterface(pictureUri);
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GPS.convert(location.getLongitude()));
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GPS.convert(location.getLatitude()));
                            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GPS.latitudeRef(location.getLatitude()));
                            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GPS.longitudeRef(location.getLongitude()));
                            exif.saveAttributes();
                        }catch(Exception e){
                        }
                    }
                }
            }

            imageView.setImageURI(Uri.parse(pictureUri));

            list.add(imageFilePath);
            currentIndex = list.size() - 1;

            updatePictureDetails(pictureUri);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode != RESULT_OK) {
            System.out.println("photo not taken");
        }
        if(requestCode == START_TO_SEARCH && resultCode == RESULT_OK){
            list = (ArrayList<String>) data.getSerializableExtra("file_list");

            if(!list.isEmpty()){
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                ImageView imageView = findViewById(R.id.imageView);
                String pictureUri = dir.getAbsolutePath() + "/" + list.get(0);
                imageView.setImageURI(Uri.parse(pictureUri));
                updatePictureDetails(pictureUri);
                currentIndex = 0;
            }
        }
    }

    // When setting button is clicked
    public void settingButton(View view){
        Intent intent = new Intent(this, DisplaySettingActivity.class);
        startActivity(intent);
    }

    // When search button is clicked
    public void searchButton(View view){
        Intent intent = new Intent(this,DisplaySearchActivity.class);
        startActivityForResult(intent,START_TO_SEARCH);
    }

    // Saves file to hard drive
    private File createImageFile() throws IOException{
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "PhotoGalleryImage_" + timeStamp + "_";
        File image = File.createTempFile(filename,".jpg",dir);

        imageFilePath = image.getName();
        return image;
    }

    // When left button is clicked
    public void leftButton(View view){
        if(currentIndex > 0 && !list.isEmpty()){
            currentIndex--;
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            ImageView imageView = findViewById(R.id.imageView);
            String pictureUri = dir.getAbsolutePath() + "/" + list.get(currentIndex);
            imageView.setImageURI(Uri.parse(pictureUri));
            updatePictureDetails(pictureUri);
        }
    }

    // When right button is clicked
    public void rightButton(View view){
        if(list.size() > currentIndex + 1){
            currentIndex++;
            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            ImageView imageView = findViewById(R.id.imageView);
            String pictureUri = dir.getAbsolutePath() + "/" + list.get(currentIndex);
            imageView.setImageURI(Uri.parse(pictureUri));
            updatePictureDetails(pictureUri);
        }
    }

    // Update the textview with picture timestamp and geocoordinates
    public void updatePictureDetails(String imageUrl){
        try {
            ExifInterface imageExif = new ExifInterface(imageUrl);
            float[] latlong = new float[2];
            if(imageExif.getLatLong(latlong)) {

                double lat = latlong[0];
                double lon = latlong[1];

                TextView detail = findViewById(R.id.imageDetailTextView);

                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                Address address;
                List<Address> list = geocoder.getFromLocation(lat, lon, 1);
                address = list.get(0);

                detail.setText(address.getAddressLine(0));
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
