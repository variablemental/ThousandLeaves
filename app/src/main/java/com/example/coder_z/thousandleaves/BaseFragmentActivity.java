package com.example.coder_z.thousandleaves;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by coder-z on 17-4-22.
 */

public abstract class BaseFragmentActivity extends FragmentActivity {

    public Bundle argument;

    @Override
    public void onCreate(Bundle savedInstanceState){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity);
        FragmentManager fm=this.getSupportFragmentManager();
        Fragment fragment=fm.findFragmentById(R.id.fragment_container);
        if(fragment==null){
            fragment=createFragment();
            if(argument!=null){
                fragment.setArguments(argument);
            }
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }

    public abstract Fragment createFragment();

}
