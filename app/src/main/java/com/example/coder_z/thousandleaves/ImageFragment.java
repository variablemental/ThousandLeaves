package com.example.coder_z.thousandleaves;

import android.app.DialogFragment;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by coder-z on 17-4-16.
 */

public class ImageFragment extends DialogFragment {
    public static String PATH_TAG="image_path";

    public static ImageFragment newInstance(String path){
        Bundle args=new Bundle();
        args.putSerializable(PATH_TAG,path);
        ImageFragment fragment=new ImageFragment();
        fragment.setArguments(args);
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE,0);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent,Bundle savedInstaceState){
        ImageView imageView=new ImageView(getActivity());
        String path=(String)getArguments().getSerializable(PATH_TAG);
        BitmapDrawable b=PictureUtil.getScaledDrawable(getActivity(),path);
        imageView.setImageDrawable(b);
        return imageView;
    }
}
