package com.chenjishi.slidedemo.base;

import android.app.Activity;
import android.app.ActivityOptions;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.chenjishi.slidedemo.R;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by chenjishi on 14-3-17.
 */
public class SlideActivity extends FragmentActivity implements SlideLayout.SlideListener {

    private SlideLayout mSlideLayout;

    private View mPreview;

    private float mInitOffset;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.slide_layout);

        mSlideLayout = (SlideLayout) findViewById(R.id.slide_layout);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mInitOffset = -(1f / 3) * metrics.widthPixels;

        View contentView = LayoutInflater.from(this).inflate(layoutResID, null);
        mSlideLayout.addView(contentView, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        mSlideLayout.setShadowResource(R.drawable.sliding_back_shadow);
        mSlideLayout.setSlidingListener(this);
        mSlideLayout.setEdgeSize((int) (metrics.density * 20));
    }

    //handle conflict with view pager
    protected void setScrollableViewId(int id) {
        if (null == mSlideLayout) return;

        View v = findViewById(id);
        if (null == v || !(v instanceof ViewPager)) return;
        mSlideLayout.setViewPager((ViewPager) v);
    }

    @Override
    public void onViewCaptured() {
        mSlideLayout.setCanSlide(false);
        Activity activity = Slide.getInstance().peek();
        if (null == activity) return;

        View v = activity.findViewById(android.R.id.content);
        if (null == v) return;

        if (v.getWidth() == 0 || v.getHeight() == 0) return;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            FrameLayout rootView = (FrameLayout) findViewById(android.R.id.content);
            if (null == mPreview) {
                mPreview = new ImageView(this);
                rootView.addView(mPreview, 0, new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            }

            Bitmap bmp = null;
            try {
                bmp = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                        Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError e) {
                bmp = null;
            }

            if (null != bmp) {
                v.draw(new Canvas(bmp));
                ((ImageView) mPreview).setImageBitmap(bmp);
                mSlideLayout.setCanSlide(true);
            }
        } else {
            mPreview = v;
            convertActivityToTranslucent();
        }
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if (slideOffset <= 0) {
            mPreview.setTranslationX(0);
        } else if (slideOffset < 1) {
            mPreview.setTranslationX(mInitOffset * (1 - slideOffset));
        } else {
            mPreview.setTranslationX(0);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        Slide.getInstance().pop();
        super.onDestroy();
    }

    private void convertActivityToTranslucent() {
        try {
            Method getActivityOptions = Activity.class.getDeclaredMethod("getActivityOptions");
            getActivityOptions.setAccessible(true);
            Object options = getActivityOptions.invoke(this);

            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> listener = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    listener = clazz;
                }
            }
            if (null == listener) return;

            Class<?>[] classArray = new Class[1];
            classArray[0] = listener;
            Object translucentListener = Proxy.newProxyInstance(listener.getClassLoader(),
                    classArray, new TranslucentChangeListener());

            Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                    listener, ActivityOptions.class);
            method.setAccessible(true);
            method.invoke(this, translucentListener, options);
        } catch (Throwable t) {
        }
    }

    private class TranslucentChangeListener implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equalsIgnoreCase("onTranslucentConversionComplete")
                    && null != args && args.length > 0) {
                Object drawComplete = args[0];
                if (drawComplete instanceof Boolean) {
                    boolean canSlide = (Boolean) drawComplete;
                    mSlideLayout.setCanSlide(canSlide);
                }
            }

            return null;
        }
    }
}
