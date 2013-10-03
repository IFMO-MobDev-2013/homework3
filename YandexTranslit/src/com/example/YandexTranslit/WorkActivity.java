package com.example.YandexTranslit;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class WorkActivity extends Activity {
    private String[] links;
    private int index;
    private ImageView[] imageView;

    private String getEnglishRussianText(String text, String flag) {
        String res = "";
        for (int i = text.indexOf("<text>") + 6; i < text.indexOf("</text>"); i++)
            res = res + text.charAt(i);
        return flag + res;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        String word = getIntent().getStringExtra("key");
        word = URLEncoder.encode(word);

        TextView textOnDisplayEN = (TextView) findViewById(R.id.textEN);
        TextView textOnDisplayRU = (TextView) findViewById(R.id.textRU);

        String send = getIntent().getStringExtra("key");
        send = URLEncoder.encode(send);

        // image
        int numberPictures = 10;
        RequestSender sender = new RequestSender(word);
        sender.start();
        try {
            sender.join();
        } catch (InterruptedException e) {}

        links = sender.getLinks();

        imageView = new ImageView[numberPictures];

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

        List<PictureDownloader> threads = new ArrayList<PictureDownloader>();

        for (index = 0; index < numberPictures; index++) {
            PictureDownloader loader = new PictureDownloader(links[index]);
            loader.start();
            threads.add(loader);
        }



        // text
        SendingText translate = new SendingText(send, "ru");
        translate.start();
        try {
            translate.join();
        } catch (InterruptedException e) {}
        textOnDisplayRU.setText(getEnglishRussianText(translate.getPage(), "RU: "));

        translate = new SendingText(send, "en");
        translate.start();
        try {
            translate.join();
        } catch (InterruptedException e) {}
        textOnDisplayEN.setText(getEnglishRussianText(translate.getPage(), "EN: "));

        for (int i = 0; i < numberPictures; i++) {
            try {
                threads.get(i).join();
            }
            catch (Exception e) {

            }
            imageView[i].setImageBitmap(threads.get(i).getBitmap());
        }
    }
}