package com.chenjishi.slidedemo.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.lang.ref.SoftReference;
import java.util.*;

/**
 * Created by jishichen on 2017/6/16.
 */
public class Slide {

    private static final Slide INSTANCE = new Slide();

    private Stack<Bitmap> stack;

    private Set<SoftReference<Bitmap>> mReusableBitmaps;

    private Slide() {
        mReusableBitmaps = Collections.synchronizedSet(new HashSet<SoftReference<Bitmap>>());
        stack = new Stack<>();
    }

    public void clear() {
        stack.clear();
        Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
        while (iterator.hasNext()) {
            Bitmap bitmap = iterator.next().get();
            if (null != bitmap) {
                bitmap.recycle();
            }
            iterator.remove();
        }
    }

    public static Slide getInstance() {
        return INSTANCE;
    }

    public Bitmap peekBitmap() {
        if (stack.empty()) return null;
        return stack.peek();
    }

    public void popBitmap() {
        if (stack.empty()) return;

        Bitmap bitmap = stack.pop();
        mReusableBitmaps.add(new SoftReference<Bitmap>(bitmap));
    }

    public void startActivity(final Context context, Intent intent) {
        new Thread() {
            @Override
            public void run() {
                captureScreen(context);
            }
        }.start();
        context.startActivity(intent);
    }

    private void captureScreen(Context context) {
        View v = ((Activity) context).findViewById(android.R.id.content);
        Bitmap bmp = null;

        if (null != mReusableBitmaps && !mReusableBitmaps.isEmpty()) {
            synchronized (mReusableBitmaps) {
                final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
                while (iterator.hasNext()) {
                    bmp = iterator.next().get();
                    if (null != bmp) {
                        iterator.remove();
                        break;
                    }
                }
            }
        } else {
            bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        if (null != bmp) {
            v.draw(new Canvas(bmp));
            stack.push(bmp);
        }
    }
}
