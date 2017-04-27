package com.example.coder_z.thousandleaves;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by coder-z on 17-4-22.
 */

public class LeafFragment extends Fragment implements Imageable {

    private ImageView mImageView;
    private EditText mTitle;
    private EditText mDetail;
    private Button func1;
    private Button func2;
    private Leaf leaf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_leaf,container,false);
        mImageView=(ImageView)v.findViewById(R.id.leaf_ImageView);
        leaf=(Leaf)getArguments().getSerializable(ForestActivity.FRAGMENT_LEAF);
        mTitle=(EditText) v.findViewById(R.id.leaf_title);
        mTitle.setText(leaf.getName());
        mDetail=(EditText)v.findViewById(R.id.leaf_detail);
        mDetail.setText(leaf.getDesciption());
        if(leaf.getImgUrl()!=null&&!leaf.getImgUrl().equals("")){
            showPhoto(leaf.getImgUrl());
        }
        return v;
    }


    @Override
    public void showPhoto(String filename) {
        String path=getActivity().getFileStreamPath(filename).getAbsolutePath();
        BitmapDrawable b= PictureUtil.getScaledDrawable(getActivity(),path);
        mImageView.setImageDrawable(b);
    }
}
