package com.example.a00914567.photogallery;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.style.TtsSpan;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

import static com.example.a00914567.photogallery.PhotoMange.FileManager.*;
import static com.example.a00914567.photogallery.PhotoMange.PhotoDetailsManager.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    Context appContext;
    File picdir;
    File fileDir;
    DateFormat dateFormat;

    @Before
    public void initialize(){
        appContext = InstrumentationRegistry.getTargetContext();
        picdir = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        fileDir = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    }

    @Test
    public void testKeywordSearch() {
        try {
            ArrayList<String> list = getPictureByDate(picdir, dateFormat.parse("2018/05/01"),dateFormat.parse("2018/05/30"));
            assertEquals(false,list.get(0).isEmpty());
            String filePath = picdir.getAbsolutePath() + "/" + list.get(0);

            setComment(filePath,"Testing Comment",fileDir);

            String comment = getComment(fileDir,picdir,filePath);
            assertEquals("Testing Comment", comment);



        }catch(Exception e){
        }
    }

}
