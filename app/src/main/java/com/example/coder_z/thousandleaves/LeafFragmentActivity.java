package com.example.coder_z.thousandleaves;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by coder-z on 17-4-22.
 */

public class LeafFragmentActivity extends BaseFragmentActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.argument=getIntent().getBundleExtra(ForestActivity.FRAGMENT_BUNDLE);
        super.onCreate(savedInstanceState);
    }
    @Override
    public Fragment createFragment() {
        return new LeafFragment();
    }
}
