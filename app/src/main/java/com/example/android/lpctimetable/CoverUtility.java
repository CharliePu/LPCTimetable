package com.example.android.lpctimetable;

// Code from @Brijesh Thakur
// https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.annotation.Nullable;

public class CoverUtility {
   public String saveCover(Bitmap bitmapImage, char classCode, Context context){
       ContextWrapper cw = new ContextWrapper(context);
       // path to /data/data/yourapp/app_data/imageDir
       File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
       // Create imageDir
       File mypath=new File(directory,"class_"+classCode);

       FileOutputStream fos = null;
       try {
           fos = new FileOutputStream(mypath);
           // Use the compress method on the BitMap object to write image to the OutputStream
           bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
       } catch (Exception e) {
           e.printStackTrace();
       } finally {
           try {
               fos.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return directory.getAbsolutePath();
   }

    public @Nullable Bitmap loadCoverFromStorage(char classCode, Context context)
    {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        try {
            File f=new File(directory, "class_"+classCode);
            return (Bitmap)BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
