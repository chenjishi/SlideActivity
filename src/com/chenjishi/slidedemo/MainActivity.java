package com.chenjishi.slidedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.chenjishi.slidedemo.base.IntentUtils;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView) findViewById(R.id.list_item);
        listView.setAdapter(new SimpleAdapter(this));
        listView.setOnItemClickListener(this);
    }

    private class SimpleAdapter extends BaseAdapter {
        private Context mContext;
        private float mDensity;

        public SimpleAdapter(Context context) {
            mContext = context;
            mDensity = context.getResources().getDisplayMetrics().density;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public String getItem(int position) {
            return "slide activity item index at " + position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;

            if (null == convertView) {
                textView = new TextView(mContext);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.f);
                textView.setTextColor(0xFF333333);

                final int paddingLeft = (int) (mDensity * 8 + .5f);
                final int paddingTop = (int) (mDensity * 20 + .5f);

                textView.setPadding(paddingLeft, paddingTop, paddingLeft, paddingTop);
            } else {
                textView = (TextView) convertView;
            }

            final String str = getItem(position);
            textView.setText(TextUtils.isEmpty(str) ? "SlideItem" : str);

            return textView;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailActivity.class);
        IntentUtils.getInstance().startActivity(this, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IntentUtils.getInstance().clear();
    }
}
