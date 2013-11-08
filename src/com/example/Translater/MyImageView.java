package com.example.Translater;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

public class MyImageView extends Activity implements View.OnClickListener {
    public static final String trans = "imo";

    Button back;
    ImageView[] mImgView;

    public List<String> getImageUrls(String word) {
        try {
            String e = "http://images.yandex.ru/yandsearch?text=" + URLEncoder.encode(word, "UTF-8") + "&isize=eq&ih=400&iw=400&nl=2&lr=2&uinfo=sw-1894-sh-919-fw-1669-fh-598-pd-1";
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(e);
            HttpResponse response = client.execute(get);
            String html = readStream(response.getEntity().getContent());
            Log.d("HTML", html);
            return new YandexImagesParser().parse(html);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public String readStream(InputStream in) {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(in);
            char[] buffer = new char[10240];
            int read;
            while ((read = reader.read(buffer)) > 0) {
                builder.append(buffer, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return builder.toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_picture);

        back = (Button) findViewById(R.id.back);
        back.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        String ThisWord = extras.getString(trans);
        TextView message = (TextView) findViewById(R.id.mes);
        message.setText("Translate: " + ThisWord);
        mImgView = new ImageView[10];

        mImgView[0] = (ImageView) findViewById(R.id.image1);
        mImgView[1] = (ImageView) findViewById(R.id.image2);
        mImgView[2] = (ImageView) findViewById(R.id.image3);
        mImgView[3] = (ImageView) findViewById(R.id.image4);
        mImgView[4] = (ImageView) findViewById(R.id.image5);
        mImgView[5] = (ImageView) findViewById(R.id.image6);
        mImgView[6] = (ImageView) findViewById(R.id.image7);
        mImgView[7] = (ImageView) findViewById(R.id.image8);
        mImgView[8] = (ImageView) findViewById(R.id.image9);
        mImgView[9] = (ImageView) findViewById(R.id.image10);

//        tv = (TextView) findViewById(R.id.textView1);


        new ImageDownload().execute(ThisWord);

    }

    public class ImageDownload extends AsyncTask<String, Bitmap, String> {

        private int count = 0;

        protected String doInBackground(String... params) {
            List<String> urls = getImageUrls(params[0]);
            for (String urlString : urls) {
                try {
                    URL url = new URL(urlString);
                    publishProgress(BitmapFactory.decodeStream(url.openStream()));
                } catch (IOException e) {
                    //Log.w(MyActivity.class.getName(), "Couldn't load image for url " + urlString);
                }
            }
            return urls.get(0);
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            if(count <= 9)
                mImgView[count++].setImageBitmap(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
        }

    }
    public void onClick(View v)
    {
        Intent intent = new Intent();
        intent.setClass(this, com.example.Translater.TranslateActivity.class);
        startActivity(intent);
        finish();
    }


}
