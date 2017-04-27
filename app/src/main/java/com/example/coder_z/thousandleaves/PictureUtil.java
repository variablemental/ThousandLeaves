package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.telecom.PhoneAccount;
import android.view.Display;
import android.view.KeyEvent;
import android.view.PixelCopy;

import java.io.FileNotFoundException;

/**
 * Created by coder-z on 17-4-16.
 */

public class PictureUtil {
    private static final int RED_RATIO=229;
    private static final int GREEN_RATIO=587;
    private static final int BLUE_RATIO=114;
    private static final int BIAS=500;

    public static BitmapDrawable getScaledDrawable(Activity activity,String path) {

        return new BitmapDrawable(activity.getResources(),getScaledBitmap(activity,path));
    }

    public static BitmapDrawable getScaledDrawable(Activity activity,Uri path) throws FileNotFoundException {
        return new BitmapDrawable(activity.getResources(),getScaledBitmap(activity,path));
    }

    public static Bitmap getScaledBitmap(Activity activity, String path){
        Display display=activity.getWindowManager().getDefaultDisplay();
        float destWidth=display.getWidth();
        float destHeight=display.getHeight();

        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path);

        float outWidth=options.outWidth;
        float outHeight=options.outHeight;

        int inSimpleSize=1;
        if(outHeight>destHeight||outWidth>destWidth){
            if(outHeight>outWidth) {
                inSimpleSize=Math.round(outHeight/destHeight);
            }else{
                inSimpleSize=Math.round(outWidth/destWidth);
            }
        }
        options=new BitmapFactory.Options();
        options.inSampleSize=inSimpleSize;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(Activity activity, Uri path) throws FileNotFoundException {
        Display display=activity.getWindowManager().getDefaultDisplay();
        float destWidth=display.getWidth();
        float destHeight=display.getHeight();

        BitmapFactory.Options options=new BitmapFactory.Options();
        Bitmap bitmap=BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(path));

        float outWidth=options.outWidth;
        float outHeight=options.outHeight;

        int inSimpleSize=1;
        if(outHeight>destHeight||outWidth>destWidth){
            if(outHeight>outWidth) {
                inSimpleSize=Math.round(outHeight/destHeight);
            }else{
                inSimpleSize=Math.round(outWidth/destWidth);
            }
        }
        options=new BitmapFactory.Options();
        options.inSampleSize=inSimpleSize;

        return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(path));
    }

    public static int[][] getBitmapToGrey(Bitmap bitmap){
        int[][] pixels=getBitmapPixels(bitmap);
        int height=pixels.length;
        int width=pixels[0].length;
        int[][] greys=new int[height][width];
        for(int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                int color=pixels[i][j];
                int red=(color&0x00ff0000)>>16;
                int green=(color&0x0000ff00)>>8;
                int blue=(color&0x000000ff);
                greys[i][j]=(red*RED_RATIO+green*GREEN_RATIO+BLUE_RATIO+blue*BIAS)/1000;
            }
        }
        return greys;
    }

    private static int[][] getBitmapPixels(Bitmap bitmap){
        int height=bitmap.getHeight();
        int width=bitmap.getWidth();
        int[][] pixels=new int[height][width];
        for(int i=0;i<height;i++){
            for (int j=0;j<width;j++){
                pixels[i][j]=bitmap.getPixel(i,j);
            }
        }
        return pixels;
    }

}
