package com.chenjishi.slidedemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

/**
 * Created by chenjishi on 14-3-19.
 */
public class IntentUtils {

    private static final IntentUtils INSTANCE = new IntentUtils();

    private Bitmap mBitmap;

    private IntentUtils() {

    }

    public void clear() {
        if (null != mBitmap && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    public static IntentUtils getInstance() {
        return INSTANCE;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void startActivity(final Context context, final Intent intent) {
        final View v = ((Activity) context).findViewById(android.R.id.content);

        if (null == mBitmap) {
            /**
             * screen capture by ARGB_8888 consume 2 times memory than RGB_565,
             * but image quality is good than RGB_565, if you want efficiency more
             * than effect, please change this to RGB_565
             */
            mBitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        }

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.draw(new Canvas(mBitmap));
                context.startActivity(intent);

            }
        }, 100);
    }
}
