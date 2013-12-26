package com.example.AMTranslator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyActivity extends Activity {
    public static final String DEFAULT_LINK = "http://www.51allout.co.uk/wp-content/uploads/2012/02/Image-not-found.gif";
    public static TextView english_textView;
    public static TextView russian_textView;
    public static ImageView[] imageView;
    public static ImageLoader imageLoader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        english_textView = (TextView) findViewById(R.id.english_textView);
        russian_textView = (TextView) findViewById(R.id.russian_textView);
        imageView = new ImageView[10];
        imageView[0] = (ImageView) findViewById(R.id.imageView);
        imageView[1] = (ImageView) findViewById(R.id.imageView1);
        imageView[2] = (ImageView) findViewById(R.id.imageView2);
        imageView[3] = (ImageView) findViewById(R.id.imageView3);
        imageView[4] = (ImageView) findViewById(R.id.imageView4);
        imageView[5] = (ImageView) findViewById(R.id.imageView5);
        imageView[6] = (ImageView) findViewById(R.id.imageView6);
        imageView[7] = (ImageView) findViewById(R.id.imageView7);
        imageView[8] = (ImageView) findViewById(R.id.imageView8);
        imageView[9] = (ImageView) findViewById(R.id.imageView9);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            EditText editText = (EditText) findViewById(R.id.editText);
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                new TextTranslator().execute(text, "RU");
                new TextTranslator().execute(text, "EN");
                new LinksSearcher().execute(text);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });
    }
}
