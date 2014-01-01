package com.example.task3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyActivity extends Activity {


    private EditText text;
    private String translation;
    private List<String> pictures;
    private String picturesJson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        text = (EditText) findViewById(R.id.edittext);
        pictures = new ArrayList<String>();
    }

    public void translate(View v) throws ExecutionException, InterruptedException, IOException, JSONException {
        String s = text.getText().toString();
        String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
        String key = "trnsl.1.1.20131002T144452Z.f718a5af8df14902.0b24c2be2db4394d4d204076387ca0cfb13db599";
        String text = s.replace(" ", "%20");
        String lang = "ru";
        url = url + "&key=" + key + "&text=" + text + "&lang=" + lang;
        translation = new Translator().execute(url).get();

        BingImageSearch bis = new BingImageSearch();
        picturesJson = bis.execute(text).get();

        changeActivity(v);
    }

    public void changeActivity(View v) {
        Intent i = new Intent(MyActivity.this, SecondActivity.class);
        i.putExtra("translation", translation);
        i.putExtra("pictures", picturesJson);
        startActivity(i);
        finish();
    }

    private class Translator extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            JSONObject json;
            String translation = "";

            try {
                InputStream is = new URL(url).openStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                json = new JSONObject(jsonText);
                translation = json.getString("text");
                translation = translation.substring(2, translation.length() - 2);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e){
                e.printStackTrace();
            }

            return translation;
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}

