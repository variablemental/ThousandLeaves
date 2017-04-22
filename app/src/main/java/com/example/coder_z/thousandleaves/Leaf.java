package com.example.coder_z.thousandleaves;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.Serializable;

/**
 * Created by coder-z on 17-4-11.
 */

public class Leaf implements Serializable /*extends RealmObject*/ {
    private int id;
    private String name;
    private String desciption;
    private String ImgUrl;

    public Leaf(int id,String name,String description,String imgUrl){
        this.id=id;
        this.name=name;
        this.desciption=description;
        this.ImgUrl=ImgUrl;
    }

    public Leaf(Leaf leaf){
        this.id=leaf.getId();
        this.name=leaf.getName();
        this.desciption=leaf.getDesciption();
        this.ImgUrl=leaf.getImgUrl();
    }

/*    public static RealmResults<Leaf> all(Realm realm){
        return realm.where(Leaf.class).findAllSorted("id", Sort.DESCENDING);
    }*/

    public  Bitmap getLeafScaledImage(Activity activity){
        return null;
    }

    public  Bitmap getLeafNormalImage(){
        return null;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getId() {
        return id;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public String getName() {
        return name;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name+":"+desciption+"@"+ImgUrl;
    }
}
