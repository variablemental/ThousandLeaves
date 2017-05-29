package com.example.coder_z.thousandleaves;

import android.app.TabActivity;
import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;

/**
 * Created by coder-z on 17-3-20.
 */

public class HomeActivity extends TabActivity implements RadioGroup.OnCheckedChangeListener {
    public static String TAG="HomeActivity";

    private static final String Home_Tag="Home";
    private static final String Forest_Tag="Forest";
    private static final String Data_Tag="Data";
    private static final String Contact_Tag="Contact";

    private RadioGroup mRadioGroup;
    private RadioButton mForestButton;
    private RadioButton mDataButton;
    private RadioButton mContactButton;
    private RadioButton mHomeButton;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRadioGroup=(RadioGroup)findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(this);
        mForestButton=(RadioButton)findViewById(R.id.main);
        mForestButton=(RadioButton)findViewById(R.id.forest);
        mDataButton=(RadioButton)findViewById(R.id.data);
        mContactButton=(RadioButton)findViewById(R.id.contact);
        tabHost=getTabHost();
        tabHost.addTab(tabHost.newTabSpec(Home_Tag).setIndicator("0").setContent(new Intent(HomeActivity.this,MainActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(Forest_Tag).setIndicator("1").setContent(new Intent(HomeActivity.this,ForestActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(Data_Tag).setIndicator("2").setContent(new Intent(HomeActivity.this,InfoActivity.class)));
        //tabHost.addTab(tabHost.newTabSpec(Contact_Tag).setIndicator("3").setContent(new Intent(HomeActivity.this,PhotoFragmentActivity.class)));
        tabHost.addTab(tabHost.newTabSpec(Contact_Tag).setIndicator("3").setContent(new Intent(HomeActivity.this,DataActivity.class)));
        tabHost.setCurrentTab(0);
        Log.d(TAG,TAG+"start");

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
            switch (i){
                case R.id.main:
                    tabHost.setCurrentTab(0);
                    break;
                case R.id.forest:
                    tabHost.setCurrentTab(1);
                    break;
                case R.id.data:
                    tabHost.setCurrentTab(2);
                    break;
                case R.id.contact:
                    tabHost.setCurrentTab(3);
                    break;
            }
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,TAG+"start");
    }
}
