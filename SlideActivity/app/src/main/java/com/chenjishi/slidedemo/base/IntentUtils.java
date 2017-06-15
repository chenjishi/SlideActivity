package com.chenjishi.slidedemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.util.Stack;

/**
 * Created by chenjishi on 14-3-19.
 */
public class IntentUtils {

    private static final IntentUtils INSTANCE = new IntentUtils();

    private Stack<Bitmap> stack;

    private IntentUtils() {
        stack = new Stack<>();
    }

    public void clear() {
        stack.clear();
    }

    public static IntentUtils getInstance() {
        return INSTANCE;
    }

    public Bitmap getBitmap() {
        return stack.pop();
    }

    public void startActivity(final Context context, final Intent intent) {
        final View v = ((Activity) context).findViewById(android.R.id.content);
        final Bitmap bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);

        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.draw(new Canvas(bmp));
                stack.push(bmp);
                context.startActivity(intent);
            }
        }, 100);
    }
}
