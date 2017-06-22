package com.chenjishi.slidedemo.base;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.chenjishi.slidedemo.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by chenjishi on 14-3-17.
 */
public class SlideActivity extends FragmentActivity implements SlideLayout.SlideListener {

    private SlideLayout mSlideLayout;

    private ImageView mPreview;

    private float mInitOffset;

    private boolean mImageSet;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.slide_layout);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mInitOffset = -(1.f / 3) * metrics.widthPixels;

        mPreview = (ImageView) findViewById(R.id.iv_preview);
        FrameLayout contentView = (FrameLayout) findViewById(R.id.content_view);
        contentView.addView(LayoutInflater.from(this).inflate(layoutResID, null),
                new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));

        mSlideLayout = (SlideLayout) findViewById(R.id.slide_layout);
        mSlideLayout.setShadowResource(R.drawable.sliding_back_shadow);
        mSlideLayout.setSlidingListener(this);
        mSlideLayout.setEdgeSize((int) (metrics.density * 20));
    }

    //handle conflict with view pager
    protected void setScrollableViewId(int id) {
        View v = findViewById(id);
        if (null == v || !(v instanceof ViewPager)) return;
        mSlideLayout.setViewPager((ViewPager) v);
    }

    @Override
    public void onViewCaptured() {
        if (mImageSet) return;
        Bitmap bmp = Slide.getInstance().peekBitmap();
        if (null != bmp) {
            mPreview.setImageBitmap(bmp);
            mImageSet = true;
        }
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

    @Override
    protected void onDestroy() {
        Slide.getInstance().popBitmap();
        super.onDestroy();
    }
}
