package com.chenjishi.slidedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.chenjishi.slidedemo.base.IntentUtils;
import com.chenjishi.slidedemo.base.SlidingActivity;

/**
 * Created by chenjishi on 14-3-17.
 */
public class DetailActivity extends SlidingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("DetailActivity");
    }

    public void onButtonClicked(View v) {
        Intent intent = new Intent(this, ImageActivity.class);
        IntentUtils.getInstance().startActivity(this, intent);
    }
}
