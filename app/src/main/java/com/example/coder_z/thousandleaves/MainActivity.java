package com.example.coder_z.thousandleaves;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements Imageable{

    public static String TAG="MainActivity";
    private static String IMG_TAG="image";
    public static final int REQUEST_PHOTO=1;


    private Button mPhotoButton;
    private Button mPhotoManage;
    private ImageView mImageView;

    private String temp_filename="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPhotoButton=(Button)findViewById(R.id.photo_button);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PhotoFragmentActivity.class);
                startActivityForResult(intent,REQUEST_PHOTO);
            }
        });
        mPhotoManage=(Button)findViewById(R.id.photo_manage);
        mPhotoManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        Log.d(TAG,TAG+"start");
        //Toast.makeText(this,"THis is Main activity",Toast.LENGTH_LONG).show();
        mImageView=(ImageView) findViewById(R.id.image_button);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path=getFileStreamPath(temp_filename).getAbsolutePath();
                ImageFragment.newInstance(path).show(getFragmentManager(),IMG_TAG);
            }
        });

    }


    @Override
    public void onActivityResult(int RequestCode,int ResultCode,Intent data){
        if(ResultCode!= Activity.RESULT_OK) return ;
        else {
            switch (RequestCode){
                case REQUEST_PHOTO:
                        String path=data.getStringExtra(PhotoFragment.FILENAME);
                        if(path!=null&&!path.equals("")) {
                            showPhoto(path);
                            temp_filename=path;
                        }
                        break;

            }
        }

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,TAG+"start");
    }

    public void showPhoto(String filename) {
        String path=this.getFileStreamPath(filename).getAbsolutePath();
        BitmapDrawable b= PictureUtil.getScaledDrawable(this,path);
        mImageView.setImageDrawable(b);
    }


}
