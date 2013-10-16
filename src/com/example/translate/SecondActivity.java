package com.example.translate;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SecondActivity extends Activity {
    String text;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);
        Bundle extras = getIntent().getExtras();
        text=extras.getString("translate");
        TextView textView=(TextView)findViewById(R.id.textView);
        textView.setText(text);
        // Next write Tenischev
        Imager image = new Imager(text);
        image.start();
        while (!image.flag){}
        ArrayList<Bitmap> pic = image.bitmaps;
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageBitmap(pic.get(0));
        ImageView imageView1 = (ImageView)findViewById(R.id.imageView2);
        imageView1.setImageBitmap(pic.get(1));
        ImageView imageView2 = (ImageView)findViewById(R.id.imageView3);
        imageView2.setImageBitmap(pic.get(2));
        ImageView imageView3 = (ImageView)findViewById(R.id.imageView4);
        imageView3.setImageBitmap(pic.get(3));
        ImageView imageView4 = (ImageView)findViewById(R.id.imageView5);
        imageView4.setImageBitmap(pic.get(4));
        ImageView imageView5 = (ImageView)findViewById(R.id.imageView6);
        imageView5.setImageBitmap(pic.get(5));
        ImageView imageView6 = (ImageView)findViewById(R.id.imageView7);
        imageView6.setImageBitmap(pic.get(6));
        ImageView imageView7 = (ImageView)findViewById(R.id.imageView8);
        imageView7.setImageBitmap(pic.get(7));
        ImageView imageView8 = (ImageView)findViewById(R.id.imageView9);
        imageView8.setImageBitmap(pic.get(8));
        ImageView imageView9 = (ImageView)findViewById(R.id.imageView10);
        imageView9.setImageBitmap(pic.get(9));

    }


}