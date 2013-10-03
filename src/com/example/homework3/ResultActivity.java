package com.example.homework3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ResultActivity extends Activity {
    private TextView textView;
    private ArrayList<ImageView> imageViews;
    private ListView listView;
    private MyAdapter myAdapter;
    private Context context;
    private final int amount = 10;

    class ImageFinder extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... params) {
            final String version = "1.0";
            final String imgsz = "small";
            final String imgfmt = "png";
            final int resPerQuery = 8;
            String[] result = new String[amount];
            int head = 0;
            int offset = 0;
            while (head < amount) {
                try {
                    final String query = params[0];
                    URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
                            "v=" + version + "&q=" + query + "&as_filetype=" + imgfmt +
                            "&rsz=" + resPerQuery + "&start=" + offset + "&imgsz=" + imgsz);
                    URLConnection connection = url.openConnection();
                    String line;
                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    while((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    JSONObject json = new JSONObject(builder.toString());
                    JSONArray resArray = json.getJSONObject("responseData").getJSONArray("results");
                    String s;
                    for (int i = 0; i < resArray.length(); i++) {
                        try {
                            s = resArray.getJSONObject(i).getString("url");
                            URLConnection imgConnection = (new URL(s)).openConnection();
                            imgConnection.setConnectTimeout(500);
                            result[head] = s;
                            head++;
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                }
                offset += resPerQuery;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String[] result) {
            for (int i = 0; i < result.length; i++) {
                new ImageDownloader().execute(result[i]);
            }
        }
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                final String imageURL = params[0];
                bitmap = BitmapFactory.decodeStream(new URL(imageURL).openConnection().getInputStream());
            } catch (Exception e) {
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ImageView imageView = new ImageView(context);
            imageView.setImageBitmap(result);
            imageViews.add(imageView);
            myAdapter.add(imageView);
        }
    }

    private class MyAdapter extends ArrayAdapter<ImageView> {

        public MyAdapter(Context context, int resource) {
            super(context, resource);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            return imageViews.get(position);
        }
    }

    private void downloadImages(String query, String translation) {
        textView = (TextView) findViewById(R.id.resTextView);
        listView = (ListView) findViewById(R.id.listView);
        myAdapter = new MyAdapter(this, R.layout.list_item);
        listView.setAdapter(myAdapter);
        imageViews = new ArrayList<ImageView>();
        context = this;
        textView.setText(translation);
        new ImageFinder().execute(query);
    }

    @Override
    protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.result);
        Intent intent = getIntent();
        downloadImages(intent.getStringExtra("query"), intent.getStringExtra("translated"));
    }
}
