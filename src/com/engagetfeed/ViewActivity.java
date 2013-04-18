package com.engagetfeed;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view); 
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("content");
            TextView tv = (TextView) findViewById(R.id.content);
            Log.d("content", value); 
            ImageGetter imgGetter = new Html.ImageGetter() {
                @Override
                public Drawable getDrawable(String source) {
                      Drawable drawable = null;
                      drawable = Drawable.createFromPath(source);
                      if (drawable != null) drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                      return drawable;
                }
         };
            tv.setText(Html.fromHtml("<h1>" + extras.getString("headline") + "</h1>" + value, imgGetter, null));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
