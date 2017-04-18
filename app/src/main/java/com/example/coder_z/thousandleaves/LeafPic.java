package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;

/**
 * Created by coder-z on 17-4-18.
 */

public class LeafPic extends Leaf {
    private Bitmap bitmap;
    private int leaf_height;
    private int leaf_width;


    LeafPic(int id,String name,String description,String imgUrl,int leaf_height,int leaf_width){
        super(id,name,description,imgUrl);
        this.leaf_height=leaf_height;
        this.leaf_width=leaf_width;
        this.bitmap=getLeafNormalImage();
    }

    LeafPic(Leaf leaf,int leaf_height,int leaf_width){
        super(leaf);
        this.leaf_height=leaf_height;
        this.leaf_width=leaf_width;
    }

    @Override
    public Bitmap getLeafScaledImage(Activity activity) {
        String path=new File(getImgUrl()).getAbsolutePath();
        return PictureUtil.getScaledBitmap(activity,path);
    }

    @Override
    public Bitmap getLeafNormalImage() {
        String path=new File(getImgUrl()).getAbsolutePath();
        return BitmapFactory.decodeFile(path);
    }

    private int getHeight(){
        int [][] pixels_grey=PictureUtil.getBitmapToGrey(bitmap);
        int height=pixels_grey.length;
        int width=pixels_grey[0].length;
        int h1=0,h2=0;
        for(int i=0;i<height;i++){
            boolean flag1=false;
            boolean flag2=false;
            for(int j=0;j<width;j++){
                if(!flag1||!flag2) {
                    if (!flag1 && pixels_grey[i][j] == 1) {
                        h1 = i;
                        flag1 = true;
                    }
                    if (!flag2 && pixels_grey[height - i - 1][j] == 1) {
                        h2 = height-i-1;
                        flag2 = true;
                    }
                }else{
                    break;
                }
            }
            if(flag1&&flag2)
                break;
        }
        return h2-h1;
    }

    private int geiWidth(){
        int[][] pixels_grey=PictureUtil.getBitmapToGrey(bitmap);
        int height=pixels_grey.length;
        int width=pixels_grey[0].length;
        int w1=0,w2=0;
        for(int i=0;i<width;i++){
            boolean flag1=false;
            boolean flag2=false;
            for(int j=0;j<height;j++){
                if(!flag1||!flag2){
                    if(!flag1&&pixels_grey[j][i]==1){
                        w1=i;
                        flag1=true;
                    }
                    if(!flag2&&pixels_grey[j][width-1-i]==1){
                        w2=width-1-i;
                        flag2=true;
                    }
                }else{
                    break;
                }
            }
            if(flag1&&flag2)
                break;
        }
        return w2-w1;
    }
}
