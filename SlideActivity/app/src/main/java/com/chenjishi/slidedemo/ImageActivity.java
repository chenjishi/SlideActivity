package com.chenjishi.slidedemo;

import android.os.Bundle;
import com.chenjishi.slidedemo.base.SlidingActivity;

/**
 * Created by chenjishi on 15/10/13.
 */
public class ImageActivity extends SlidingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        setTitle("ImageActivity");
    }
}
