package com.example.Translate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Show extends Activity {
    TextView textView;
    LinearLayout layout;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show);

        Button button = (Button) findViewById(R.id.back);
        textView = (TextView) findViewById(R.id.output);
        textView.setTextSize(50);
        textView.setTextColor(Color.BLACK);

        Intent intent = getIntent();
        String word = intent.getStringExtra("word");

        Translater translater = new Translater(word);
        PictureFinder pictureFinder = new PictureFinder(word);
        layout = (LinearLayout) findViewById(R.id.imagesLayout);
        translater.execute();
        pictureFinder.execute();



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public class Translater extends AsyncTask<Void, Void, String> {
        String word;

        Translater(String word) {
            this.word = URLEncoder.encode(word);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            textView.setText(result);
        }

        @Override
        protected String doInBackground(Void... parameter) {
            String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=trnsl.1.1.20130923T111105Z.d461ad359432a880.b1bbf827f59c7207f6aa527fc2b67ecdf52daf68&text=" + word + "&lang=en-ru";
            String Text;
            try {
                InputStream in = new java.net.URL(url).openStream();
                java.util.Scanner str = new java.util.Scanner(in).useDelimiter("\\A");
                Text = str.hasNext() ? str.next() : "";

            } catch (Exception e) {
                return "can't connect to internet";
            }
            int i1 = Text.indexOf('[') + 2;
            int i2 = Text.indexOf(']') - 1;
            return Text.substring(i1, i2);
        }
    }

    public class PictureFinder extends AsyncTask<Void, Void, ArrayList<Drawable>> {

        private String sampleLink = "https://www.dropbox.com/s/x3k56wluyq9eygc/f_error.gif";
        private String requestString;

        public PictureFinder(String request) {
            requestString = request;
        }

        @Override
        public void onPostExecute(ArrayList<Drawable> images) {
            super.onPostExecute(images);
            if (images == null) {
                TextView errorText = (TextView) findViewById(R.id.errorWithPics);
                errorText.setTextColor(Color.RED);
                errorText.setTextSize(50);
                errorText.setText("error");
            } else {
                for (int i = 0; i < 10; i++) {
                    ImageView imageView = new ImageView(Show.this);
                    imageView.setImageDrawable(images.get(i));
                    layout.addView(imageView);
                    layout.invalidate();
                }
                textView.setTextSize(Color.BLACK);

            }

        }

        @Override
        public ArrayList<Drawable> doInBackground(Void... parameter) {
            requestString = URLEncoder.encode(requestString);
            ArrayList<String> pictures = new ArrayList<String>();
            URLConnection connection;
            String requestURL = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=";
            try {
                for (int j = 0; j < 3; j++) {
                    URL pageURL = new URL(requestURL + requestString + "&start=" + (4 * j));
                    connection = pageURL.openConnection();
                    connection.connect();
                    BufferedReader in;
                    in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));
                    JSONObject request = new JSONObject(in.readLine());
                    request = request.getJSONObject("responseData");
                    JSONArray images = request.getJSONArray("results");
                    for (int i = 0; i < (j == 2 ? 2 : 4); i++) {
                        JSONObject cur = images.getJSONObject(i);
                        pictures.add(cur.getString("url"));
                    }
                }
                ArrayList<Drawable> imageList = new ArrayList<Drawable>();
                for (int i = 0; i < 10; i++) {
                    imageList.add(getImageFromURL(pictures.get(i)));
                }

                return imageList;
            } catch (Exception e) {
                return null;
            }
        }


        public Drawable getImageFromURL(String imageURL) throws IOException {
            Drawable drawable;
            try {
                drawable = Drawable.createFromStream((InputStream) new URL(imageURL).getContent(), "Pic");
            } catch (FileNotFoundException e) {
                drawable = Drawable.createFromStream((InputStream) new URL(sampleLink).getContent(), "errorPic");
            }
            return drawable;
        }

    }

}