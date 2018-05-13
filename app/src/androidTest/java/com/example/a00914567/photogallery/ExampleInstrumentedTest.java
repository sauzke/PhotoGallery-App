package com.example.a00914567.photogallery;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.text.style.TtsSpan;

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
    @Test
    public void testCaptionStorage() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        File dir = appContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File fileDir = appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            ArrayList<String> list = getPictureByDate(dir, dateFormat.parse("2018/05/01"),dateFormat.parse("2018/05/30"));
            assertEquals(false,list.get(0).isEmpty());
            String filePath = dir.getAbsolutePath() + "/" + list.get(0);

            setComment(filePath,"Testing Comment",fileDir);

            String comment = getComment(fileDir,dir,filePath);
            assertEquals("Testing Comment", comment);
        }catch(Exception e){
        }
    }

}
