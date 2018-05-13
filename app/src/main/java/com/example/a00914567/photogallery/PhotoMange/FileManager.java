package com.example.a00914567.photogallery.PhotoMange;

import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class FileManager implements PhotoManageImp {
    public static ArrayList<String> getPictureByKeyword(File fileDir, String keyword)throws FileNotFoundException{
//        File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File commentData = new File(fileDir.getAbsolutePath() + "/PhotoGallery.dat");
        ArrayList<String> files = new ArrayList<String>();

        if(commentData.exists()) {
            Scanner scanner = new Scanner(commentData);

            while (scanner.hasNextLine()) {
                String temp = scanner.nextLine();
                String[] parts = temp.split(";");

                if (parts[1].matches(keyword)) {
                    files.add(parts[0]);
                }
            }
        }

        return files;
    }

    public static ArrayList<String> getPictureByDate(File picDir, Date startDate, Date endDate) throws ParseException {
        //File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String[] list = picDir.list();
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

    public static File createImageFile(File dir) throws IOException {
        //File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "PhotoGalleryImage_" + timeStamp + "_";
        File image = File.createTempFile(filename,".jpg",dir);

        //imageFilePath = image.getName();
        return image;
    }
}
