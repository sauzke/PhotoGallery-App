package com.example.a00914567.photogallery.PhotoMange;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a00914567.photogallery.Util.GPS;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PhotoDetailsManager implements PhotoManageImp {
    public static void updatePictureDetails(String imageUrl, TextView detail, EditText captions, Geocoder geocoder, File fileDir, File picDir){
        try {
            ExifInterface imageExif = new ExifInterface(imageUrl);
            float[] latlong = new float[2];
            if(imageExif.getLatLong(latlong)) {

                double lat = latlong[0];
                double lon = latlong[1];

                //TextView detail = findViewById(R.id.imageDetailTextView);

                //Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                Address address;
                List<Address> list = geocoder.getFromLocation(lat, lon, 1);
                address = list.get(0);

                String displayText = address.getAddressLine(0) + "\n" + imageExif.getAttribute(ExifInterface.TAG_DATETIME);
                //EditText captions = findViewById(R.id.captionEditText);

                captions.setText(getComment(fileDir, picDir, imageUrl));

                detail.setText(displayText);
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static String getComment(File dir, File picDir, String fileName) throws IOException{
        //File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File commentData = new File(dir.getAbsolutePath() + "/PhotoGallery.dat");
        String comment = "";
        //File picDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if(commentData.exists()) {
            Scanner scanner = new Scanner(commentData);
            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                String[] parts = temp.split(";");

                String absFilePath = picDir + "/" + parts[0];

                if (absFilePath.trim().matches(fileName.trim())) {
                    comment = parts[1];
                    break;
                }
            }
        }

        return comment;
    }

    public static void setComment(String fileName, String comment, File dir) throws IOException{
        //File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File commentData = new File(dir.getAbsolutePath() + "/PhotoGallery.dat");

        if(!commentData.exists()){
            commentData.createNewFile();
        }

        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(commentData, true)));
        out.println(fileName + ";" + comment);
        out.close();
    }

    public static void setLocation(Context context, String pictureUri){
        if(ContextCompat.checkSelfPermission( context, android.Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location != null){
                    try {
                        ExifInterface exif = new ExifInterface(pictureUri);
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, GPS.convert(location.getLongitude()));
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, GPS.convert(location.getLatitude()));
                        exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, GPS.latitudeRef(location.getLatitude()));
                        exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, GPS.longitudeRef(location.getLongitude()));
                        String timeStamp = new SimpleDateFormat("yyyy/MM/dd h:mm a").format(new Date());
                        exif.setAttribute(ExifInterface.TAG_DATETIME, timeStamp);
                        exif.saveAttributes();
                    }catch(Exception e){
                    }
                }
            }
        }
    }
}
