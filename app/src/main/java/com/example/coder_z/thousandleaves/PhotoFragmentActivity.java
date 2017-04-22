package com.example.coder_z.thousandleaves;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by coder-z on 17-3-14.
 */

public class PhotoFragmentActivity extends BaseFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment createFragment() {
       return new PhotoFragment();
    }

}
