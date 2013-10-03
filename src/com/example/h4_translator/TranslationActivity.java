package com.example.h4_translator;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: carbo_000
 * Date: 01.10.13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class TranslationActivity extends Activity {
    TextView textOut;
    URL url;
    String text,calling;
    Intent intent2;

    private class Task extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(calling);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                httpCon.connect();
                BufferedReader buffReader = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
                String current = buffReader.readLine();
                int n = current.length();
                current = current.substring(36,n - 3);
                return current;
            } catch (Exception e) {
                return null;
            }
        }
        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                result = "NO TRANSLATION. SORRY";
            }
            textOut.setText(text + " ->(ru)" + result);
        }
    }

    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation);
        calling = "https://translate.yandex.net/api/v1.5/tr.json/translate?lang=en-ru&key=trnsl.1.1.20131003T083136Z.93467e8308f15ba0.6f7c26deb3b324d5cf803d43513497cb571b4891&text=";
        textOut = (TextView)findViewById(R.id.textTranslation);
        text = getIntent().getStringExtra("str");
        calling += text;
        new Task().execute(text);
    }

    public void showimages(View v)
    {
        intent2 = new Intent(this, ShowImages.class);
        intent2.putExtra("entext",text);
        startActivity(intent2);
    }

}