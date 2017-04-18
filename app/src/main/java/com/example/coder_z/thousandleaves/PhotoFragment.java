package com.example.coder_z.thousandleaves;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;
import java.util.jar.Manifest;

/**
 * Created by coder-z on 17-3-12.
 */

public class PhotoFragment extends Fragment {

    public static String TAG="PhotoFragment";
    public static String FILENAME="FileName";

    private Context context=getActivity();
    private Button mTake;
    private Camera mCamera;
    private View ProgressContainer;
    private SurfaceView mSurfaceView;

    private Camera.PictureCallback mpictureCallback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            boolean success=true;
            String filename= UUID.randomUUID().toString()+".jpg";
            FileOutputStream out=null;
            try {
                out=getActivity().openFileOutput(filename,Context.MODE_PRIVATE);
                out.write(bytes);
            }catch (Exception e){
                Log.e(TAG,"Error on output");
                success=false;
            }finally {
                if(success){
                    Intent intent=new Intent();
                    intent.putExtra(FILENAME,filename);
                    getActivity().setResult(Activity.RESULT_OK,intent);
                    Toast.makeText(getActivity(),"saved in"+filename,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Error occurred",Toast.LENGTH_SHORT).show();
                    getActivity().setResult(Activity.RESULT_CANCELED);
                }
            }
            getActivity().finish();
        }
    };

    private Camera.ShutterCallback shutterCallback=new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            ProgressContainer.setVisibility(View.VISIBLE);
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.photo_fragment_layout,container,false);

        ProgressContainer=(ProgressBar)v.findViewById(R.id.progressBar);
        ProgressContainer.setVisibility(View.INVISIBLE);
        mTake=(Button)v.findViewById(R.id.button_take);
        mTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(shutterCallback,null,mpictureCallback);
            }
        });
        mSurfaceView=(SurfaceView)v.findViewById(R.id.surface_view);
        final SurfaceHolder holder=mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if(mCamera!=null){
                        mCamera.setPreviewDisplay(holder);
                    }
                }catch(Exception e){
                    Log.e(TAG,"Error in surfaceCreate");
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int width, int height) {
                if(mCamera==null)
                    return;
                Camera.Parameters parameters=mCamera.getParameters();
                Camera.Size size=getBestSupportSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPreviewSize(size.width,size.height);
                size=getBestSupportSize(parameters.getSupportedPreviewSizes(),width,height);
                parameters.setPictureSize(size.width,size.height);
                mCamera.setParameters(parameters);
                try {
                    mCamera.startPreview();
                }catch (Exception e){
                    Log.e(TAG,"Error in surfaceChange");
                    mCamera.release();
                    mCamera=null;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                   if(mCamera!=null){
                       mCamera.stopPreview();
                   }
            }
        });


        return v;
    }

    private Camera.Size getBestSupportSize(List<Camera.Size> sizes,int width,int height){
        Camera.Size temp_size=sizes.get(0);
        int largestSize=temp_size.width*temp_size.height;
        for(Camera.Size size:sizes){
            int Coverage=size.height*size.width;
            if(Coverage>largestSize) {
                largestSize=Coverage;
                temp_size=size;
            }
        }
        return temp_size;
    }

    @TargetApi(9)
    @Override
    public void onResume(){
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.GINGERBREAD)
            mCamera=mCamera.open(0);
        else
            mCamera=mCamera.open();

    }

    @Override
    public void onPause(){
        super.onPause();
        if(mCamera!=null){
            mCamera.release();
            mCamera=null;
        }
    }

}

