package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by coder-z on 17-4-27.
 */

public class Leaf implements Serializable {
    private int    id;
    private String Name;
    private String description;
    private String ImgUrl;

    Leaf(int id,String Name,String description,String ImgUrl){
        this.id=id;
        this.Name=Name;
        this.description=description;
        this.ImgUrl=ImgUrl;
    }

    Leaf(Leaf leaf){
        this.id=leaf.getId();
        this.Name=leaf.getName();
        this.description=leaf.getDescription();
        this.ImgUrl=leaf.getImgUrl();
    }

    public String getName() {
        return Name;
    }

    public int getId() {
        return id;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgUrl(String imgUrl) {
        this.ImgUrl=imgUrl;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLocation() {
        if(!description.contains("[")){
            return null;
        }
        String[] s=description.split("\\[");
        return s[s.length-1];
    }


    public Bitmap getLeafScaledImage(Activity activity){
        return null;
    }
    public Bitmap getLeafNormalImage(){
        return null;
    }

}
