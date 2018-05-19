package com.htetaung.backgroundapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by HtetAung on 5/18/18.
 */

public class Util {

    private static final String BACKCAMERA="BACK_CAMERA";
    private static final String CAMERA="CAMERA";


    public static void backCameraActivate(Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences(CAMERA, MODE_PRIVATE).edit();
        editor.putBoolean(BACKCAMERA,true);
    }

    public static void frontCameraActivate(Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences(CAMERA, MODE_PRIVATE).edit();
        editor.putBoolean(BACKCAMERA,false);
    }

    /**
     * for the first time front camera will be activated
     */
    public static boolean isBackCamera(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences(CAMERA, MODE_PRIVATE);
        return sharedPreferences.getBoolean(BACKCAMERA,false);
    }

    public static Bitmap getBitmapFromByteArray(@NonNull byte[] img) {

        if (img != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(img);
            return BitmapFactory.decodeStream(imageStream);
        } else {
            return null;
        }
    }
}
