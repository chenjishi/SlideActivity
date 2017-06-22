package com.chenjishi.slidedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.chenjishi.slidedemo.base.SlideActivity;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by jishichen on 2017/6/22.
 */
public class ViewPagerActivity extends SlideActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new SimplePagerAdapter());
        setScrollableViewId(R.id.view_pager);
    }

    private class SimplePagerAdapter extends PagerAdapter {

        private float density;

        public SimplePagerAdapter() {
            density = getResources().getDisplayMetrics().density;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
            TextView v = new TextView(ViewPagerActivity.this);
            v.setTextColor(0xFF333333);
            v.setText(R.string.description);
            v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            v.setLineSpacing(4 * density, 1);
            int padding = (int) (density * 8 + .5);
            v.setPadding(padding, padding, padding, padding);
            container.addView(v, lp);
            return v;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
