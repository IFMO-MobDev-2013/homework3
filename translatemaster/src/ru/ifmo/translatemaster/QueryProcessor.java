package ru.ifmo.translatemaster;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("text");
        AsyncTask<String, Void, String> translator = new Translator();
        translator.execute(text);
        ImageReceiver imageReceiver = new ImageReceiver();
        imageReceiver.execute(text);
        setContentView(R.layout.translation);
    }

    private void setResult(String result) {
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(result);
    }

    private void setImages(ArrayList<Bitmap> bitmaps){
        GridView gridView = (GridView) findViewById(R.id.gridview);
        if(bitmaps == null){
            gridView.setNumColumns(1);
            bitmaps = new ArrayList<Bitmap>();
            bitmaps.add(BitmapFactory.decodeResource(getResources(), R.drawable.error));
            Toast.makeText(this, "Error occurred while receiving images", Toast.LENGTH_LONG).show();
        }
        if(bitmaps.size() == 0){
            Toast.makeText(this, "Images weren't found", Toast.LENGTH_LONG).show();
        }
        gridView.setAdapter(new ImageAdapter(this, bitmaps, 250, 250, 0));
    }

    private class ImageReceiver extends AsyncTask<String, Void, ArrayList<Bitmap>>{
        private Bitmap getBitmap(String url) throws IOException {
            InputStream iStream = new URL(url).openStream();
            return BitmapFactory.decodeStream(iStream);
        }

        @Override
        protected ArrayList<Bitmap> doInBackground(String... strings) {
            String query;
            try {
                query = "http://images.yandex.ru/yandsearch?text=" + URLEncoder.encode(strings[0], "ISO-8859-1");
                // Fetching html page from Yandex
                String result = "";
                URL url = new URL(query);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    result += scanner.nextLine();
                }
                // Parsing response
                Pattern pattern = Pattern.compile("<img class=\"[^\"]*preview[^\"]*\" (?:alt=\".*?\" )*src=\"(.*?)\"");
                Matcher matcher = pattern.matcher(result);
                ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
                // Fetching images
                for(int i = 0; i < 10; i++){
                    if(!matcher.find()){
                        break;
                    }
                    try{
                        bitmaps.add(getBitmap(matcher.group(1)));
                    } catch (IOException e){
                        // Trying receive other pics
                        --i;
                    }
                }
                return bitmaps;

            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> result) {
            super.onPostExecute(result);
            setImages(result);
        }
    }

    private class Translator extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... text) {
            String query, result = "";
            try {
                query = "https://translate.yandex.net/api/v1.5/tr/translate?key=trnsl.1.1.20130922T074611Z.7a00ee38626ce635.00b0b8c45901e1bed9cf08e1275b7985ce51a66f&text=" + URLEncoder.encode(text[0], "ISO-8859-1") + "&lang=en-ru";
            } catch (UnsupportedEncodingException e) {
                return "Sorry, something went wrong";
            }
            try {
                URL url = new URL(query);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    result += scanner.nextLine();
                }
            } catch (IOException e) {
                return "Error while connecting to server";
            }
            int from = -1, to = -1;
            try {
                for (int i = 0; i < result.length(); i++) {
                    if (i + 6 < result.length() && result.substring(i, i + 6).equals("<text>")) {
                        from = i + 6;
                        break;
                    }
                }
                if (from == -1) {
                    return null;
                }
                for (int i = from; i < result.length(); i++) {
                    if (i + 7 < result.length() && result.substring(i, i + 7).equals("</text>")) {
                        to = i;
                        break;
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                return "Sorry, something went wrong";
            }
            return "Translation: " + result.substring(from, to);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            setResult(result);
        }
    }
}
