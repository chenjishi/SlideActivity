package com.chenjishi.slidedemo.base;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.chenjishi.slidedemo.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by chenjishi on 14-3-17.
 */
public class SlideActivity extends FragmentActivity implements SlideLayout.SlidingListener {
    private View mPreview;

    private float mInitOffset;
    private boolean hideTitle = false;
    private int titleResId = -1;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.slide_layout);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        LayoutInflater inflater = LayoutInflater.from(this);
        mInitOffset = -(1.f / 3) * metrics.widthPixels;

        mPreview = findViewById(R.id.iv_preview);
        FrameLayout contentView = (FrameLayout) findViewById(R.id.content_view);

        if (!hideTitle) {
            int resId = -1 == titleResId ? R.layout.title_layout : titleResId;
            inflater.inflate(resId, contentView);
        }

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(MATCH_PARENT,
                MATCH_PARENT, Gravity.BOTTOM);
        final int marginTop = hideTitle ? 0 : (int) (metrics.density * 48.f + .5f);
        lp.setMargins(0, marginTop, 0, 0);
        contentView.addView(inflater.inflate(layoutResID, null), lp);

        final SlideLayout slideLayout = (SlideLayout) findViewById(R.id.slide_layout);
        slideLayout.setShadowResource(R.drawable.sliding_back_shadow);
        slideLayout.setSlidingListener(this);
        slideLayout.setEdgeSize((int) (metrics.density * 20));

        Bitmap bmp = Slide.getInstance().peekBitmap();
        if (null != bmp) {
            if (Build.VERSION.SDK_INT >= 16) {
                mPreview.setBackground(new BitmapDrawable(bmp));
            } else {
                mPreview.setBackgroundDrawable(new BitmapDrawable(bmp));
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        ((TextView) findViewById(R.id.label_title)).setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        ((TextView) findViewById(R.id.label_title)).setText(titleId);
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if (slideOffset <= 0) {
        } else if (slideOffset < 1) {
            mPreview.setTranslationX(mInitOffset * (1 - slideOffset));
        } else {
            mPreview.setTranslationX(0);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    protected void setContentView(int layoutResID, int titleResId) {
        this.titleResId = titleResId;
        setContentView(layoutResID);
    }

    protected void setContentView(int layoutResID, boolean hideTitle) {
        this.hideTitle = hideTitle;
        setContentView(layoutResID);
    }

    @Override
    protected void onDestroy() {
        Slide.getInstance().popBitmap();
        super.onDestroy();
    }
}
