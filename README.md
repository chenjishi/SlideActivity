SlideActivity
=============

A iOS like slide back Activity framework, slide to exit Activity. Now the demo use ARGB_8888 to save bitmap when capture screen, memory consume will be 2 times larger than RGB_565, but captured screen more better than RGB_565, if you want efficency most, change ARGB_8888 to RGB_565.

If you have any idea to improve this project, any commits welcomed:)

download demo in Google Play [SlideActivity](https://play.google.com/store/apps/details?id=com.chenjishi.slidedemo&hl=zh-CN)
##ScreenShot
<p align="center">
  <img src="https://raw.github.com/chenjishi/SlideActivity/master/demo.gif" 
  alt="slideback" height="400" width="240"/>
</p>

##Usage

###1.Extend SlidingActivity
```
public class DetailActivity extends SlidingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("DetailActivity");
    }
}
```

###2.Start your Intent
```
public void onButtonClicked(View v) {
        Intent intent = new Intent(this, ImageActivity.class);
        IntentUtils.getInstance().startActivity(this, intent);
    }
```

### All Done!


