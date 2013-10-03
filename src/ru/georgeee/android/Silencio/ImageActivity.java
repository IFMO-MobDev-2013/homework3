package ru.georgeee.android.Silencio;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Created with IntelliJ IDEA.
 * User: marsermd
 * Date: 03.10.13
 * Time: 5:20
 * To change this template use File | Settings | File Templates.
 */
public class ImageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_actitvity);

        Bundle extra = getIntent().getExtras();

        ((ImageView)findViewById(R.id.fullscreen_image)).setImageResource(extra.getInt("image", -1));
    }
}
