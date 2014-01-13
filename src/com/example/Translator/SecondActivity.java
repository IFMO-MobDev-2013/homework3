package com.example.Translator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondActivity extends Activity {
    public ImageView[] imageview = new ImageView[12];
    ImageDownloader downloader;
    String word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        TextView txtInfo_en = (TextView) findViewById(R.id.textView1);
        word = getIntent().getExtras().getString("English_word");
        txtInfo_en.setText(word);

        TextView txtInfo_ru = (TextView) findViewById(R.id.textView3);
        word = getIntent().getExtras().getString("Russian_word");
        txtInfo_ru.setText(word);

        imageview[0] = (ImageView) findViewById(R.id.imageView);
        imageview[1] = (ImageView) findViewById(R.id.imageView1);
        imageview[2] = (ImageView) findViewById(R.id.imageView2);
        imageview[3] = (ImageView) findViewById(R.id.imageView3);
        imageview[4] = (ImageView) findViewById(R.id.imageView4);
        imageview[5] = (ImageView) findViewById(R.id.imageView5);
        imageview[6] = (ImageView) findViewById(R.id.imageView6);
        imageview[7] = (ImageView) findViewById(R.id.imageView7);
        imageview[8] = (ImageView) findViewById(R.id.imageView8);
        imageview[9] = (ImageView) findViewById(R.id.imageView9);
        imageview[10] = (ImageView) findViewById(R.id.imageView10);
        imageview[11] = (ImageView) findViewById(R.id.imageView11);

        TextView tvInfo = (TextView) findViewById(R.id.tvInfo);

        downloader = new ImageDownloader(word, imageview, tvInfo);
        downloader.execute();
    }

    public void onBackPressed() {
        Intent intent = new Intent(SecondActivity.this, MyActivity.class);
        startActivity(intent);
        finish();
    }
}


