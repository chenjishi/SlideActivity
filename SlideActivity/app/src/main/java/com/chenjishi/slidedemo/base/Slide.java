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
        captureScreen(context);
        context.startActivity(intent);
    }

    private void captureScreen(Context context) {
        View v = ((Activity) context).findViewById(android.R.id.content);
        Bitmap bmp = null;

        /**
         * find reusable bitmap, sometimes bitmap maybe recycled by soft reference,
         * so we need to check item.get() to see if bitmap exist
         */
        if (null != mReusableBitmaps && !mReusableBitmaps.isEmpty()) {
            synchronized (mReusableBitmaps) {
                final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
                while (iterator.hasNext()) {
                    SoftReference<Bitmap> item = iterator.next();
                    if (null != item) {
                        bmp = item.get();
                        if (null != bmp) {
                            iterator.remove();
                            break;
                        } else {
                            iterator.remove();
                        }
                    }
                }
            }
        }

        if (null == bmp) {
            bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                    Bitmap.Config.ARGB_8888);
        }

        v.draw(new Canvas(bmp));
        stack.push(bmp);
    }
}
